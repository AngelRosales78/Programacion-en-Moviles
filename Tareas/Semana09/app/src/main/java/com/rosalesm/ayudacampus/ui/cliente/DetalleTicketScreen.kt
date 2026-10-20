package com.rosalesm.ayudacampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.TicketDetalleViewModel
@Composable
fun DetalleTicketScreen(vm: TicketDetalleViewModel,onVolver: () -> Unit) {
    val sla by vm.sla.collectAsState()
    val detalle by vm.ticket.collectAsState()
    val error by vm.error.collectAsState()
    val ocupado by vm.ocupado.collectAsState()
    var texto by rememberSaveable(vm.ticketId) { mutableStateOf("") }
    var final by rememberSaveable(vm.ticketId) { mutableStateOf("") }
    var nota by rememberSaveable(vm.ticketId) { mutableIntStateOf(0) }
    val d=detalle
    LazyColumn(Modifier.fillMaxSize().padding(horizontal=20.dp),verticalArrangement=Arrangement.spacedBy(12.dp),contentPadding=PaddingValues(bottom=24.dp)) {
        item { Cabecera("Ticket #${vm.ticketId}",onVolver=onVolver) }
        if(d==null) item { Text("Cargando ticket…") }
        else {
            item { Card { Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                Text(d.ticket.titulo,style=MaterialTheme.typography.titleLarge)
                Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { EstadoChip(d.ticket.estado); PrioridadChip(d.ticket.prioridad) }
                Text("${d.categoria.nombre} · ${d.ticket.ubicacion}")
                Text(d.ticket.descripcion)
                SlaTexto(sla[d.ticket.id])
            } } }
            item { StepperTicket(d.ticket.estado) }
            item { Text("Conversación",style=MaterialTheme.typography.titleMedium) }
            items(d.comentarios.sortedWith(compareBy({ it.fecha },{ it.id })),key={ it.id }) { BurbujaComentario(it,it.autorId==vm.usuarioActual.id) }
            if(d.ticket.estado!="CERRADO") item {
                Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
                    AyudaTextField(texto,{ texto=it },"Escribe un comentario",singleLine=false)
                    Button(onClick={ vm.agregarComentario(texto) { texto="" } },enabled=texto.isNotBlank() && !ocupado) { Text("Enviar comentario") }
                }
            }
            if(d.ticket.estado=="RESUELTO") item {
                Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.tertiaryContainer)) {
                    Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                        Text("✓ Soporte marcó este ticket como resuelto. ¿El problema se solucionó?")
                        Text("Califica la atención")
                        Estrellas(nota,!ocupado) { nota=it }
                        AyudaTextField(final,{ final=it },"Comentario final (opcional)",singleLine=false)
                        AyudaButton("Confirmar y cerrar",{ vm.cerrarConCalificacion(nota,final) },enabled=nota in 1..5 && !ocupado)
                        OutlinedButton(onClick={ vm.reabrir() },enabled=!ocupado) { Text("Reabrir ticket") }
                    }
                }
            }
            if(d.ticket.estado=="CERRADO") item { Text("Calificación recibida: ${d.ticket.calificacion}/5"); Estrellas(d.ticket.calificacion ?: 0,false) }
        }
        item { MensajeError(error) }
    }
}
