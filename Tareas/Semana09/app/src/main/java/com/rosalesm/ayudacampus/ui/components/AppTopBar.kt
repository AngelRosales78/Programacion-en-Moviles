package com.rosalesm.ayudacampus.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
@Composable
fun Cabecera(titulo: String,subtitulo: String="",onVolver: (() -> Unit)?=null) {
    Column(Modifier.fillMaxWidth().padding(vertical=12.dp)) {
        if(onVolver!=null) TextButton(onClick=onVolver) { Text("‹ Volver") }
        Text(titulo,style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold)
        if(subtitulo.isNotEmpty()) Text(subtitulo,style=MaterialTheme.typography.bodyMedium,color=MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
@Composable
fun MensajeError(texto: String?) { if(texto!=null) Text(texto,color=MaterialTheme.colorScheme.error) }
