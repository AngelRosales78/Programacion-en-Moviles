package com.rosalesm.ayudacampus.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun EntradaComentario(texto: String,onTexto: (String)->Unit,soporte: Boolean=false,enabled: Boolean=true,onEnviar: ()->Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical=10.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value=texto,onValueChange=onTexto,placeholder={ Text(if(soporte) "Responder al estudiante…" else "Escribe un comentario…",style=MaterialTheme.typography.bodySmall) },modifier=Modifier.weight(1f),shape=RoundedCornerShape(26.dp),textStyle=MaterialTheme.typography.bodyMedium,maxLines=4,colors=OutlinedTextFieldDefaults.colors(unfocusedContainerColor=MaterialTheme.colorScheme.surface,focusedContainerColor=MaterialTheme.colorScheme.surface))
        FilledIconButton(onClick=onEnviar,enabled=enabled && texto.isNotBlank(),shape=CircleShape,modifier=Modifier.size(44.dp)) { Icon(Icons.AutoMirrored.Filled.Send,if(soporte) "Enviar respuesta" else "Enviar comentario",Modifier.size(21.dp)) }
    }
}
