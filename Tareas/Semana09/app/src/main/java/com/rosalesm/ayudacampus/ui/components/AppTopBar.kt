package com.rosalesm.ayudacampus.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
@Composable
fun Cabecera(titulo: String,subtitulo: String="",onVolver: (() -> Unit)?=null) {
    if(onVolver!=null) Row(Modifier.fillMaxWidth().height(48.dp),verticalAlignment=Alignment.CenterVertically) {
        IconButton(onClick=onVolver,modifier=Modifier.size(36.dp)) { Icon(Icons.AutoMirrored.Outlined.ArrowBack,"Volver",Modifier.size(20.dp)) }
        Box(Modifier.weight(1f),contentAlignment=Alignment.Center) { Text(titulo,style=MaterialTheme.typography.titleMedium) }
        Spacer(Modifier.width(36.dp))
    } else Row(Modifier.fillMaxWidth().padding(top=20.dp,bottom=8.dp),verticalAlignment=Alignment.CenterVertically) {
        Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(3.dp)) {
            Text(titulo,style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold)
            if(subtitulo.isNotEmpty()) Text(subtitulo,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Outlined.Notifications,null,Modifier.size(20.dp))
    }
}
@Composable
fun MensajeError(texto: String?) { if(texto!=null) Text(texto,color=MaterialTheme.colorScheme.error,style=MaterialTheme.typography.bodySmall) }
