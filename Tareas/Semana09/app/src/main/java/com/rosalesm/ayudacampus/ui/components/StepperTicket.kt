package com.rosalesm.ayudacampus.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
@Composable
fun StepperTicket(estado: String) {
    val lista=listOf("ABIERTO","EN_PROCESO","RESUELTO","CERRADO"); val actual=lista.indexOf(estado)
    Box(Modifier.fillMaxWidth().padding(vertical=10.dp)) {
        Row(Modifier.fillMaxWidth().padding(horizontal=38.dp).padding(top=9.dp)) { (0..2).forEach { i -> Box(Modifier.weight(1f).height(2.dp).background(if(i<actual) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)) } }
        Row(Modifier.fillMaxWidth()) { lista.forEachIndexed { i,e -> Column(Modifier.weight(1f),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(9.dp)) {
            val c=if(i<actual) Color(0xFF16A34A) else if(i==actual) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
            Box(Modifier.size(20.dp).background(c,CircleShape).border(if(i==actual) 3.dp else 2.dp,if(i==actual) MaterialTheme.colorScheme.primaryContainer else if(i>actual) MaterialTheme.colorScheme.outlineVariant else c,CircleShape),contentAlignment=Alignment.Center) { if(i<actual) Icon(Icons.Default.Check,null,Modifier.size(13.dp),tint=Color.White) }
            Text(etiqueta(e),style=MaterialTheme.typography.labelSmall,color=if(i<=actual) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant)
        } } }
    }
}
