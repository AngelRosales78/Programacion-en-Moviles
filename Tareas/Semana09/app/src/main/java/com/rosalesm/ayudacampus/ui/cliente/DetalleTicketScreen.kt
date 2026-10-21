package com.rosalesm.ayudacampus.ui.cliente
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.TicketDetalleViewModel
@Composable
fun DetalleTicketScreen(vm: TicketDetalleViewModel,onVolver: ()->Unit) {
    val detalle by vm.ticket.collectAsState(); val sla by vm.sla.collectAsState(); val error by vm.error.collectAsState(); val ocupado by vm.ocupado.collectAsState()
    var texto by rememberSaveable(vm.ticketId) { mutableStateOf("") }; var final by rememberSaveable(vm.ticketId) { mutableStateOf("") }; var nota by rememberSaveable(vm.ticketId) { mutableIntStateOf(0) }
    val d=detalle
    Column(Modifier.fillMaxSize().padding(horizontal=18.dp)) {
        Cabecera("Ticket #${vm.ticketId.toString().padStart(4,'0')}",onVolver=onVolver)
        LazyColumn(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(12.dp),contentPadding=PaddingValues(top=8.dp,bottom=12.dp)) {
            if(d==null) item { Text("Cargando ticket…") }
            else {
                item { Tarjeta {
                    Text(d.ticket.titulo,style=MaterialTheme.typography.titleMedium)
                    Row(horizontalArrangement=Arrangement.spacedBy(5.dp)) { EstadoChip(d.ticket.estado); PrioridadChip(d.ticket.prioridad) }
                    Row(horizontalArrangement=Arrangement.spacedBy(6.dp),verticalAlignment=Alignment.CenterVertically) { Icon(iconoCategoria(d.categoria.icono),null,Modifier.size(14.dp)); Text("${d.categoria.nombre} · ${d.ticket.ubicacion}",style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant) }
                    Text(d.ticket.descripcion,style=MaterialTheme.typography.bodyMedium)
                    SlaTexto(sla[d.ticket.id])
                } }
                if(d.ticket.estado=="RESUELTO") item {
                    Surface(color=MaterialTheme.colorScheme.tertiaryContainer,contentColor=MaterialTheme.colorScheme.onTertiaryContainer,shape=RoundedCornerShape(13.dp)) { Text("✓ Soporte marcó este ticket como resuelto. ¿El problema se solucionó?",Modifier.padding(14.dp),style=MaterialTheme.typography.bodyMedium) }
                } else item { StepperTicket(d.ticket.estado) }
                if(d.ticket.estado!="RESUELTO") item { Text("Conversación",style=MaterialTheme.typography.titleMedium) }
                items(d.comentarios.sortedWith(compareBy({ it.fecha },{ it.id })),key={ it.id }) { BurbujaComentario(it,it.autorId==vm.usuarioActual.id) }
                if(d.ticket.estado=="RESUELTO") {
                    item { Tarjeta { Column(Modifier.fillMaxWidth(),horizontalAlignment=Alignment.CenterHorizontally) { Text("Califica la atención",style=MaterialTheme.typography.titleMedium); Estrellas(nota,!ocupado) { nota=it }; Text("$nota de 5 estrellas",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant) } } }
                    item { AyudaTextField(final,{ final=it },"Comentario final (opcional)",singleLine=false) }
                    item { AyudaButton("Confirmar y cerrar",{ vm.cerrarConCalificacion(nota,final) },enabled=nota in 1..5 && !ocupado) }
                    item { OutlinedButton(onClick={ vm.reabrir() },enabled=!ocupado,modifier=Modifier.fillMaxWidth().height(46.dp),shape=RoundedCornerShape(11.dp),border=BorderStroke(1.dp,MaterialTheme.colorScheme.error),colors=ButtonDefaults.outlinedButtonColors(contentColor=MaterialTheme.colorScheme.error)) { Text("Reabrir ticket") } }
                }
                if(d.ticket.estado=="CERRADO") item { Tarjeta { Text("Calificación recibida: ${d.ticket.calificacion}/5"); Estrellas(d.ticket.calificacion ?: 0,false) } }
            }
            item { MensajeError(error) }
        }
        if(d!=null && d.ticket.estado!="CERRADO" && d.ticket.estado!="RESUELTO") EntradaComentario(texto,{ texto=it },enabled=!ocupado) { vm.agregarComentario(texto) { texto="" } }
    }
}
