package com.rosalesm.ayudacampus.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.data.local.entity.Comentario
import java.text.SimpleDateFormat
import java.util.*
@Composable
fun BurbujaComentario(c: Comentario,propio: Boolean) {
    Column(Modifier.fillMaxWidth(),horizontalAlignment=if(propio) Alignment.End else Alignment.Start) {
        Card(colors=CardDefaults.cardColors(containerColor=if(propio) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),modifier=Modifier.widthIn(max=300.dp)) {
            Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) {
                Text("${c.autorNombre} · ${SimpleDateFormat("dd/MM HH:mm",Locale.getDefault()).format(Date(c.fecha))}",style=MaterialTheme.typography.labelSmall)
                Text(c.texto)
            }
        }
    }
}
