package com.rosalesm.ayudacampus.ui.components
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun PrioridadChip(prioridad: String) {
    val c=colorPrioridad(prioridad)
    Surface(color=c.copy(alpha=.13f),shape=RoundedCornerShape(10.dp)) { Text(etiqueta(prioridad),Modifier.padding(horizontal=7.dp,vertical=3.dp),style=MaterialTheme.typography.labelSmall,color=c) }
}
