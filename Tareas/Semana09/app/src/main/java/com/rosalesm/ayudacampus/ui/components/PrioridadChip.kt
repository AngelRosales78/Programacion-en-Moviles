package com.rosalesm.ayudacampus.ui.components
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
@Composable
fun PrioridadChip(prioridad: String) { SuggestionChip(onClick={},label={ Text(etiqueta(prioridad),color=colorPrioridad(prioridad)) }) }
