package com.rosalesm.ayudacampus.ui.components.graficos
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
data class DatoGrafico(val etiqueta: String,val valor: Float,val color: Color)
@Composable
fun GraficoDona(datos: List<DatoGrafico>) {
    val total=datos.sumOf { it.valor.toDouble() }.toFloat()
    if(total==0f) { Text("Aún no hay tickets"); return }
    Row(horizontalArrangement=Arrangement.spacedBy(20.dp)) {
        Canvas(Modifier.size(132.dp).padding(14.dp)) {
            var inicio=-90f
            datos.forEach { d -> val angulo=d.valor/total*360f; drawArc(d.color,inicio,angulo,false,style=Stroke(25f)); inicio+=angulo }
        }
        Column { datos.forEach { Text("${it.etiqueta}: ${it.valor.toInt()}",color=it.color,style=MaterialTheme.typography.bodySmall) } }
    }
}
