package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.Aviso

private const val MAXIMO = 20

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    Tarjeta(titulo = "Historial") {
        if (avisos.isEmpty()) {
            Text("Sin avisos todavía", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            avisos.take(MAXIMO).forEachIndexed { indice, aviso ->
                if (indice > 0) HorizontalDivider()
                FilaAviso(aviso)
            }
        }
    }
}

@Composable
private fun FilaAviso(aviso: Aviso) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Etiqueta(
            texto = aviso.origen.texto,
            fondo = MaterialTheme.colorScheme.primaryContainer,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(aviso.titulo, fontWeight = FontWeight.Bold)
            Text(aviso.texto, style = MaterialTheme.typography.bodySmall)
        }
        Text(
            aviso.hora,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
