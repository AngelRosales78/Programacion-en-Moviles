package com.rosalesm.ayudacampus.ui.soporte
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.model.*
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.AtenderViewModel
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AtenderTicketScreen(vm: AtenderViewModel,onVolver: ()->Unit) {
    val detalle by vm.ticket.collectAsState(); val estado by vm.estado.collectAsState(); val prioridad by vm.prioridad.collectAsState(); val sla by vm.sla.collectAsState(); val error by vm.error.collectAsState(); val ocupado by vm.ocupado.collectAsState()
    var texto by rememberSaveable { mutableStateOf("") }; val d=detalle
    Column(Modifier.fillMaxSize().padding(horizontal=18.dp)) {
        Cabecera("Atender ticket #${d?.ticket?.id?.toString()?.padStart(4,'0') ?: ""}",onVolver=onVolver)
        LazyColumn(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(12.dp),contentPadding=PaddingValues(top=8.dp,bottom=16.dp)) {
            if(d!=null) {
                val cerrado=d.ticket.estado=="CERRADO"
                item { Tarjeta { Text(d.ticket.titulo,style=MaterialTheme.typography.titleMedium); Text("${d.cliente.nombre} · ${d.ticket.ubicacion} · ${d.categoria.nombre}",style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant); Text(d.ticket.descripcion,style=MaterialTheme.typography.bodyMedium); SlaTexto(sla[d.ticket.id]) } }
                item { Text("Estado",style=MaterialTheme.typography.titleMedium)
                    FlowRow(horizontalArrangement=Arrangement.spacedBy(5.dp)) { EstadoTicket.entries.forEach { e -> Pill(etiqueta(e.name),(estado?.name ?: d.ticket.estado)==e.name,{ vm.cambiarEstado(e) },color=colorEstado(e.name),enabled=!cerrado && e!=EstadoTicket.CERRADO && !ocupado) } }
                }
                item { Text("Prioridad",style=MaterialTheme.typography.titleMedium)
                    Row(horizontalArrangement=Arrangement.spacedBy(6.dp)) { Prioridad.entries.forEach { p -> Pill(etiqueta(p.name),(prioridad?.name ?: d.ticket.prioridad)==p.name,{ vm.cambiarPrioridad(p) },Modifier.weight(1f),colorPrioridad(p.name),enabled=!cerrado && !ocupado) } }
                }
                item { Text("Conversación",style=MaterialTheme.typography.titleMedium) }
                items(d.comentarios.sortedWith(compareBy({ it.fecha },{ it.id })),key={ it.id }) { BurbujaComentario(it,it.autorId==vm.usuario.id) }
                if(!cerrado) {
                    item { EntradaComentario(texto,{ texto=it },soporte=true,enabled=!ocupado) { vm.responder(texto) { texto="" } } }
                    item { AyudaButton("Guardar cambios",{ vm.guardar(onVolver) },enabled=!ocupado) }
                } else item { Text("Ticket cerrado · Calificación: ${d.ticket.calificacion}/5") }
            }
            item { MensajeError(error) }
        }
    }
}
