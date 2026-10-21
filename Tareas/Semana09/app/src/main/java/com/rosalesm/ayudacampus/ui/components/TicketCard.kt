package com.rosalesm.ayudacampus.ui.components
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.data.local.relation.TicketConDetalle
import com.rosalesm.ayudacampus.model.EstadoSla
import com.rosalesm.ayudacampus.model.fechaRelativa
fun etiqueta(valor: String)=when(valor) { "ABIERTO"->"Abierto"; "EN_PROCESO"->"En proceso"; "RESUELTO"->"Resuelto"; "CERRADO"->"Cerrado"; "ALTA"->"Alta"; "MEDIA"->"Media"; "BAJA"->"Baja"; else->valor }
fun iconoCategoria(icono: String): ImageVector=when(icono) { "pc"->Icons.Outlined.Computer; "code"->Icons.Outlined.Code; "wifi"->Icons.Outlined.Wifi; "key"->Icons.Outlined.VpnKey; "printer"->Icons.Outlined.Print; else->Icons.AutoMirrored.Outlined.Label }
fun colorPrioridad(p: String)=when(p) { "ALTA"->Color(0xFFDC2626); "MEDIA"->Color(0xFFB45309); else->Color(0xFF6B7280) }
@Composable
fun TicketCard(detalle: TicketConDetalle,mostrarCliente: Boolean=false,sla: EstadoSla?=null,onClick: ()->Unit) {
    Surface(onClick=onClick,modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(12.dp),color=MaterialTheme.colorScheme.surface,border=BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant)) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            Box(Modifier.width(5.dp).fillMaxHeight().background(colorPrioridad(detalle.ticket.prioridad)))
            Row(Modifier.padding(11.dp),horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                IconoCaja(iconoCategoria(detalle.categoria.icono),tamano=34)
                Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                    Text(detalle.ticket.titulo,fontWeight=FontWeight.Bold,style=MaterialTheme.typography.titleSmall,maxLines=2,overflow=TextOverflow.Ellipsis)
                    Text("#${detalle.ticket.id.toString().padStart(4,'0')}  ·  ${if(mostrarCliente) detalle.cliente.nombre else detalle.ticket.ubicacion.ifBlank { detalle.categoria.nombre }}",style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant,maxLines=1,overflow=TextOverflow.Ellipsis)
                    Spacer(Modifier.height(3.dp))
                    Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(5.dp)) {
                        EstadoChip(detalle.ticket.estado); PrioridadChip(detalle.ticket.prioridad)
                        Spacer(Modifier.weight(1f)); Text(fechaRelativa(detalle.ticket.fecha),style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    SlaTexto(sla)
                }
            }
        }
    }
}
