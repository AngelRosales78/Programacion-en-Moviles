package com.rosalesm.ayudacampus.ui.components
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
fun colorEstado(e: String)=when(e) { "ABIERTO"->Color(0xFFD97706); "EN_PROCESO"->Color(0xFF2563EB); "RESUELTO"->Color(0xFF16A34A); else->Color(0xFF6B7280) }
@Composable
fun EstadoChip(estado: String) {
    val c=colorEstado(estado)
    Surface(color=c.copy(alpha=.13f),shape=RoundedCornerShape(10.dp)) { Text(etiqueta(estado),Modifier.padding(horizontal=7.dp,vertical=3.dp),style=MaterialTheme.typography.labelSmall,color=c) }
}
