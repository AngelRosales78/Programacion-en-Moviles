package com.rosalesm.ayudacampus.ui.components
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.rosalesm.ayudacampus.model.EstadoSla
@Composable
fun SlaTexto(sla: EstadoSla?) {
    if(sla!=null) Text(sla.texto,style=MaterialTheme.typography.labelMedium,color=if(sla.vencido) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
}
