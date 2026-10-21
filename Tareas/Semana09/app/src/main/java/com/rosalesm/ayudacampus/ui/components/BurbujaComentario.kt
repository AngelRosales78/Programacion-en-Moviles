package com.rosalesm.ayudacampus.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.data.local.entity.Comentario
import java.text.SimpleDateFormat
import java.util.*
@Composable
fun BurbujaComentario(c: Comentario,propio: Boolean) {
    Column(Modifier.fillMaxWidth(),horizontalAlignment=if(propio) Alignment.End else Alignment.Start) {
        val fondo=if(propio) MaterialTheme.colorScheme.primary else if(c.autorRol=="ADMIN") (if(MaterialTheme.colorScheme.surface.luminance()>.5f) Color(0xFFEDE4FF) else Color(0xFF382451)) else MaterialTheme.colorScheme.surface
        val letra=if(propio) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        Surface(Modifier.widthIn(max=272.dp),color=fondo,contentColor=letra,shape=RoundedCornerShape(topStart=16.dp,topEnd=16.dp,bottomStart=if(propio) 16.dp else 4.dp,bottomEnd=if(propio) 4.dp else 16.dp)) {
            Column(Modifier.padding(horizontal=13.dp,vertical=10.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) {
                Text("${if(propio) "Tú" else if(c.autorRol=="ADMIN") "Soporte TI" else c.autorNombre} · ${SimpleDateFormat("HH:mm",Locale.getDefault()).format(Date(c.fecha))}",style=MaterialTheme.typography.labelSmall,color=letra.copy(alpha=.7f))
                Text(c.texto,style=MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
