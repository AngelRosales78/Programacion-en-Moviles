package com.rosalesm.ayudacampus.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun StepperTicket(estado: String) {
    val estados=listOf("ABIERTO","EN_PROCESO","RESUELTO","CERRADO")
    val actual=estados.indexOf(estado)
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(4.dp)) {
        estados.forEachIndexed { i,e -> Column(Modifier.weight(1f)) {
            LinearProgressIndicator(progress={ if(i<=actual) 1f else 0f },modifier=Modifier.fillMaxWidth())
            Text(etiqueta(e),style=MaterialTheme.typography.labelSmall)
        } }
    }
}
