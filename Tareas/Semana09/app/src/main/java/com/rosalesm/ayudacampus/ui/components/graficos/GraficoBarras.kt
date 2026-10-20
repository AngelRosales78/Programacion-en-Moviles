package com.rosalesm.ayudacampus.ui.components.graficos
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun GraficoBarras(datos: List<DatoGrafico>) {
    val maximo=(datos.maxOfOrNull { it.valor } ?: 0f).coerceAtLeast(1f)
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
        datos.forEach { d ->
            Text("${d.etiqueta} · ${d.valor.toInt()}",style=MaterialTheme.typography.bodySmall)
            LinearProgressIndicator(progress={ d.valor/maximo },modifier=Modifier.fillMaxWidth().height(12.dp),color=d.color)
        }
    }
}
