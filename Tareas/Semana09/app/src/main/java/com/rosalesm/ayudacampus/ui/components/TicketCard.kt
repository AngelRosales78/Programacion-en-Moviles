package com.rosalesm.ayudacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.data.local.relation.TicketConDetalle

fun etiqueta(valor: String) = when(valor) {
    "ABIERTO" -> "Abierto"; "EN_PROCESO" -> "En proceso"; "RESUELTO" -> "Resuelto"; "CERRADO" -> "Cerrado"
    "ALTA" -> "Alta"; "MEDIA" -> "Media"; "BAJA" -> "Baja"; else -> valor
}
fun iconoCategoria(icono: String): ImageVector = when(icono) {
    "pc" -> Icons.Outlined.Computer; "code" -> Icons.Outlined.Code; "wifi" -> Icons.Outlined.Wifi
    "key" -> Icons.Outlined.VpnKey; "printer" -> Icons.Outlined.Print; else -> Icons.Outlined.Label
}
fun colorPrioridad(p: String) = when(p) { "ALTA" -> Color(0xFFDC2626); "MEDIA" -> Color(0xFFB45309); else -> Color(0xFF6B7280) }
@Composable
fun TicketCard(detalle: TicketConDetalle,mostrarCliente: Boolean=false,sla: com.rosalesm.ayudacampus.model.EstadoSla?=null,onClick: () -> Unit) {
    Card(onClick=onClick,modifier=Modifier.fillMaxWidth()) {
        Row {
            Box(Modifier.width(5.dp).height(170.dp).background(colorPrioridad(detalle.ticket.prioridad)))
            Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(7.dp)) {
                Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                    Icon(iconoCategoria(detalle.categoria.icono),null,tint=MaterialTheme.colorScheme.primary)
                    Text("#${detalle.ticket.id} · ${detalle.categoria.nombre}",style=MaterialTheme.typography.labelMedium)
                }
                Text(detalle.ticket.titulo,fontWeight=FontWeight.Bold)
                Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { EstadoChip(detalle.ticket.estado); PrioridadChip(detalle.ticket.prioridad) }
                SlaTexto(sla)
                Text(if(mostrarCliente) "${detalle.cliente.nombre} · ${detalle.ticket.ubicacion}" else detalle.ticket.ubicacion,style=MaterialTheme.typography.bodySmall)
            }
        }
    }
}
