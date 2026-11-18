package com.tecsup.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Mejora IA 1: solo dibuja los chips; la lista filtrada la calcula el ViewModel
@Composable
fun FiltroCategorias(
    categorias: List<String>,
    seleccionada: String?,
    onSeleccion: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = seleccionada == null,
                onClick = { onSeleccion(null) },
                label = { Text("Todos") }
            )
        }
        items(categorias) { cat ->
            FilterChip(
                selected = seleccionada == cat,
                onClick = { onSeleccion(if (seleccionada == cat) null else cat) },
                label = { Text(cat) }
            )
        }
    }
}
