package com.rosalesm.ayudacampus.ui.components
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
@Composable
fun EstadoChip(estado: String) {
    val color=when(estado) { "ABIERTO" -> Color(0xFFD97706); "EN_PROCESO" -> Color(0xFF2563EB); "RESUELTO" -> Color(0xFF16A34A); else -> Color(0xFF6B7280) }
    SuggestionChip(onClick={},label={ Text(etiqueta(estado),color=color) })
}
