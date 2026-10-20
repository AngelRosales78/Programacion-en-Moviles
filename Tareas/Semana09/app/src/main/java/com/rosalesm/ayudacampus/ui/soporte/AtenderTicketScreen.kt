package com.rosalesm.ayudacampus.ui.soporte
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.model.*
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.AtenderViewModel
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AtenderTicketScreen(vm: AtenderViewModel,onVolver: () -> Unit) {
    val detalle by vm.ticket.collectAsState(); val estado by vm.estado.collectAsState(); val prioridad by vm.prioridad.collectAsState()
    val error by vm.error.collectAsState(); val ocupado by vm.ocupado.collectAsState()
    var texto by rememberSaveable { mutableStateOf("") }
    val d=detalle
    LazyColumn(Modifier.fillMaxSize().padding(horizontal=20.dp),verticalArrangement=Arrangement.spacedBy(12.dp),contentPadding=PaddingValues(bottom=24.dp)) {
        item { Cabecera("Atender ticket #${d?.ticket?.id ?: ""}",onVolver=onVolver) }
        if(d!=null) {
            val cerrado=d.ticket.estado=="CERRADO"
            item { Card { Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                Text(d.ticket.titulo,style=MaterialTheme.typography.titleLarge)
                Text("${d.cliente.nombre} · ${d.cliente.correo}")
                Text("${d.categoria.nombre} · ${d.ticket.ubicacion}")
                Text(d.ticket.descripcion)
            } } }
            item { Text("Estado",style=MaterialTheme.typography.titleMedium)
                FlowRow(horizontalArrangement=Arrangement.spacedBy(6.dp)) { EstadoTicket.entries.forEach { e ->
                    FilterChip(selected=(estado?.name ?: d.ticket.estado)==e.name,onClick={ vm.cambiarEstado(e) },enabled=!cerrado && e!=EstadoTicket.CERRADO && !ocupado,label={ Text(etiqueta(e.name)) })
                } }
            }
            item { Text("Prioridad",style=MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { Prioridad.entries.forEach { p ->
                    FilterChip(selected=(prioridad?.name ?: d.ticket.prioridad)==p.name,onClick={ vm.cambiarPrioridad(p) },enabled=!cerrado && !ocupado,label={ Text(etiqueta(p.name)) })
                } }
            }
            item { Text("Conversación",style=MaterialTheme.typography.titleMedium) }
            items(d.comentarios.sortedWith(compareBy({ it.fecha },{ it.id })),key={ it.id }) { BurbujaComentario(it,it.autorId==vm.usuario.id) }
            if(!cerrado) {
                item { AyudaTextField(texto,{ texto=it },"Responder al estudiante",singleLine=false) }
                item { Button(onClick={ vm.responder(texto) { texto="" } },enabled=texto.isNotBlank() && !ocupado) { Text("Enviar respuesta") } }
                item { AyudaButton("Guardar cambios",{ vm.guardar(onVolver) },enabled=!ocupado) }
            } else item { Text("Ticket cerrado · Calificación: ${d.ticket.calificacion}/5") }
        }
        item { MensajeError(error) }
    }
}
