package com.rosalesm.tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    destinoActual: String,
    cantidadFavoritos: Int,
    onNavegar: (String) -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AR",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Angel Rosales",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "angel.rosales.m@tecsup.edu.pe",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        HorizontalDivider()
        Spacer(modifier = Modifier.height(12.dp))

        val opciones = listOf(
            "Inicio" to Icons.Default.Home,
            "Mis pedidos" to Icons.Default.ShoppingCart,
            "Favoritos" to Icons.Default.Favorite,
            "Perfil" to Icons.Default.Person,
            "Cerrar sesión" to Icons.Default.ExitToApp
        )

        opciones.forEach { (titulo, icono) ->
            NavigationDrawerItem(
                label = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = titulo)
                        if (titulo == "Favoritos" && cantidadFavoritos > 0) {
                            Badge {
                                Text(text = cantidadFavoritos.toString())
                            }
                        }
                    }
                },
                selected = destinoActual == titulo,
                onClick = { onNavegar(titulo) },
                icon = { Icon(imageVector = icono, contentDescription = titulo) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}