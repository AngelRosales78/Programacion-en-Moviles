package com.rosalesm.ayudacampus.ui.components
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
@Composable
fun Estrellas(valor: Int,enabled: Boolean=true,onCambio: (Int) -> Unit = {}) {
    Row { (1..5).forEach { i -> IconButton(onClick={ onCambio(i) },enabled=enabled) {
        Icon(if(i<=valor) Icons.Default.Star else Icons.Outlined.StarOutline, "$i estrellas",tint=MaterialTheme.colorScheme.primary)
    } } }
}
