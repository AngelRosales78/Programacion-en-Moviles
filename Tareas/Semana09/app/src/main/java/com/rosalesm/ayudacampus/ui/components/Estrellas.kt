package com.rosalesm.ayudacampus.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
@Composable
fun Estrellas(valor: Int,enabled: Boolean=true,onCambio: (Int)->Unit={}) {
    Row { (1..5).forEach { i -> IconButton(onClick={ onCambio(i) },enabled=enabled,modifier=Modifier.size(42.dp)) {
        Icon(if(i<=valor) Icons.Default.Star else Icons.Outlined.StarOutline,"$i estrellas",Modifier.size(33.dp),tint=if(i<=valor) Color(0xFFF59E0B) else Color(0xFFD1D5DB))
    } } }
}
