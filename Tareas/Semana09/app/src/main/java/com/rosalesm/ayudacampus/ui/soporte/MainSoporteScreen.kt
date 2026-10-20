package com.rosalesm.ayudacampus.ui.soporte

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rosalesm.ayudacampus.viewmodel.SesionViewModel

@Composable
fun MainSoporteScreen(
    sesionVm: SesionViewModel,
    onCambiarAPerfilEstudiante: () -> Unit = {},
    onCerrarSesion: () -> Unit
) {
    var pestanaSeleccionada by remember { mutableStateOf(0) }
    var ticketSeleccionadoNumero by remember { mutableStateOf<String?>(null) }
    var mostrarFormularioNuevaCategoria by remember { mutableStateOf(false) }

    // Estado persistente de categorías en memoria
    val categoriasList = remember {
        mutableStateListOf(
            CategoriaItemData("1", "Hardware", "6 tickets", Icons.Outlined.Computer, true),
            CategoriaItemData("2", "Software", "4 tickets", Icons.Outlined.Code, true),
            CategoriaItemData("3", "Red / Wi-Fi", "9 tickets", Icons.Outlined.Wifi, true),
            CategoriaItemData("4", "Cuenta", "5 tickets", Icons.Outlined.VpnKey, true),
            CategoriaItemData("5", "Impresión", "3 tickets", Icons.Outlined.Print, false)
        )
    }

    val colorActivo = Color(0xFF7C3AED)
    val colorInactivo = Color(0xFF94A3B8)

    if (ticketSeleccionadoNumero != null) {
        AtenderTicketScreen(
            numeroTicket = ticketSeleccionadoNumero ?: "#0012",
            onVolver = { ticketSeleccionadoNumero = null }
        )
    } else if (mostrarFormularioNuevaCategoria) {
        FormularioCategoriaScreen(
            onGuardarCategoria = { nombre, icono, activa ->
                val nueva = CategoriaItemData(
                    id = System.currentTimeMillis().toString(),
                    nombre = nombre,
                    cantidadTickets = "0 tickets",
                    icono = icono,
                    activa = activa
                )
                categoriasList.add(nueva)
                mostrarFormularioNuevaCategoria = false
            },
            onVolver = { mostrarFormularioNuevaCategoria = false }
        )
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = pestanaSeleccionada == 0,
                        onClick = { pestanaSeleccionada = 0 },
                        icon = { Icon(Icons.Outlined.BarChart, contentDescription = null) },
                        label = { Text("Panel", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorActivo,
                            selectedTextColor = colorActivo,
                            unselectedIconColor = colorInactivo,
                            unselectedTextColor = colorInactivo,
                            indicatorColor = Color(0xFFEDE4FF)
                        )
                    )

                    NavigationBarItem(
                        selected = pestanaSeleccionada == 1,
                        onClick = { pestanaSeleccionada = 1 },
                        icon = { Icon(Icons.Outlined.ConfirmationNumber, contentDescription = null) },
                        label = { Text("Tickets", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorActivo,
                            selectedTextColor = colorActivo,
                            unselectedIconColor = colorInactivo,
                            unselectedTextColor = colorInactivo,
                            indicatorColor = Color(0xFFEDE4FF)
                        )
                    )

                    NavigationBarItem(
                        selected = pestanaSeleccionada == 2,
                        onClick = { pestanaSeleccionada = 2 },
                        icon = { Icon(Icons.Outlined.Category, contentDescription = null) },
                        label = { Text("Categorías", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorActivo,
                            selectedTextColor = colorActivo,
                            unselectedIconColor = colorInactivo,
                            unselectedTextColor = colorInactivo,
                            indicatorColor = Color(0xFFEDE4FF)
                        )
                    )

                    NavigationBarItem(
                        selected = pestanaSeleccionada == 3,
                        onClick = { pestanaSeleccionada = 3 },
                        icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                        label = { Text("Ajustes", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorActivo,
                            selectedTextColor = colorActivo,
                            unselectedIconColor = colorInactivo,
                            unselectedTextColor = colorInactivo,
                            indicatorColor = Color(0xFFEDE4FF)
                        )
                    )
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier.padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                when (pestanaSeleccionada) {
                    0 -> PanelScreen(
                        onVerTicketsPrioridadAlta = { pestanaSeleccionada = 1 }
                    )
                    1 -> BandejaScreen(
                        onTicketClick = { numero ->
                            ticketSeleccionadoNumero = numero
                        }
                    )
                    2 -> CategoriasScreen(
                        categoriasList = categoriasList,
                        onNuevaCategoriaClick = {
                            mostrarFormularioNuevaCategoria = true
                        }
                    )
                    3 -> AjustesScreen(
                        sesionVm = sesionVm,
                        onCambiarAPerfilEstudiante = onCambiarAPerfilEstudiante,
                        onCerrarSesion = onCerrarSesion
                    )
                }
            }
        }
    }
}