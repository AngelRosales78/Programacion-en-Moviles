package com.rosalesm.tecsupstore

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@Composable
fun TarjetaProducto(
    producto: Producto
) {
    // Hito 1: Estado expanded para controlar la visibilidad del menú
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        // Importante: El DropdownMenu debe ir dentro de un Box junto al botón que lo activa
        Box(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "S/ ${String.format("%.2f", producto.precio)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Contenedor del icono de 3 puntos y su menú desplegable
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Opciones del producto"
                        )
                    }

                    // Hitos 2 y 3: DropdownMenu con íconos y separadores
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Favoritos") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.FavoriteBorder,
                                    contentDescription = null
                                )
                            },
                            onClick = { expanded = false }
                        )
                        HorizontalDivider() // Divisor / Separador
                        DropdownMenuItem(
                            text = { Text("Compartir") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null
                                )
                            },
                            onClick = { expanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Reportar") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null
                                )
                            },
                            onClick = { expanded = false }
                        )
                    }
                }
            }
        }
    }
}