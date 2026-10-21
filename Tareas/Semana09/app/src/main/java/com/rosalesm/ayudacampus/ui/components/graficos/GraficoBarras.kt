package com.rosalesm.ayudacampus.ui.components.graficos
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
@Composable
fun GraficoBarras(datos: List<DatoGrafico>) {
    val maximo=(datos.maxOfOrNull { it.valor } ?: 0f).coerceAtLeast(1f)
    Row(Modifier.fillMaxWidth().height(134.dp),horizontalArrangement=Arrangement.spacedBy(6.dp),verticalAlignment=Alignment.Bottom) {
        datos.forEach { d -> Column(Modifier.weight(1f),horizontalAlignment=Alignment.CenterHorizontally) {
            Text("${d.valor.toInt()}",style=MaterialTheme.typography.labelSmall)
            Canvas(Modifier.fillMaxWidth().height(88.dp)) {
                val alto=(d.valor/maximo)*size.height
                drawRoundRect(d.color,topLeft=Offset(size.width*0.1f,size.height-alto),size=Size(size.width*0.8f,alto))
            }
            Text(d.etiqueta,style=MaterialTheme.typography.labelSmall,maxLines=2)
        } }
    }
}
