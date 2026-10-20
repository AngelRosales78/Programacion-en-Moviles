package com.rosalesm.ayudacampus.ui.cliente

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rosalesm.ayudacampus.viewmodel.MisTicketsViewModel
import com.rosalesm.ayudacampus.viewmodel.NuevoTicketViewModel
import com.rosalesm.ayudacampus.viewmodel.SesionViewModel

@Composable
fun MainClienteScreen(
    misTicketsVm: MisTicketsViewModel,
    nuevoTicketVm: NuevoTicketViewModel,
    sesionVm: SesionViewModel,
    onCerrarSesion: () -> Unit
) {
    var pestañaSeleccionada by remember { mutableStateOf(0) }
    var ticketSeleccionadoId by remember { mutableStateOf<String?>(null) }

    val sesionEstado by sesionVm.sesion.collectAsState()
    val esModoOscuro by sesionVm.esModoOscuro.collectAsState()
    val tickets by misTicketsVm.tickets.collectAsState()

    val colorActivo = Color(0xFF00838F)
    val colorInactivo = Color(0xFF94A3B8)

    val ticketActual = tickets.find { it.id == ticketSeleccionadoId }

    if (ticketActual != null) {
        DetalleTicketScreen(
            ticket = ticketActual,
            onVolver = { ticketSeleccionadoId = null },
            onEnviarComentario = { texto ->
                misTicketsVm.agregarComentario(ticketActual.id, texto)
            },
            onCalificar = { _, _ ->
                ticketSeleccionadoId = null
            }
        )
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = pestañaSeleccionada == 0,
                        onClick = { pestañaSeleccionada = 0 },
                        icon = { Icon(Icons.Outlined.Menu, contentDescription = null) },
                        label = { Text("Tickets", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorActivo,
                            selectedTextColor = colorActivo,
                            unselectedIconColor = colorInactivo,
                            unselectedTextColor = colorInactivo,
                            indicatorColor = Color(0xFFE0F7FA)
                        )
                    )

                    NavigationBarItem(
                        selected = pestañaSeleccionada == 1,
                        onClick = { pestañaSeleccionada = 1 },
                        icon = { Icon(Icons.Outlined.AddBox, contentDescription = null) },
                        label = { Text("Nuevo", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorActivo,
                            selectedTextColor = colorActivo,
                            unselectedIconColor = colorInactivo,
                            unselectedTextColor = colorInactivo,
                            indicatorColor = Color(0xFFE0F7FA)
                        )
                    )

                    NavigationBarItem(
                        selected = pestañaSeleccionada == 2,
                        onClick = { pestañaSeleccionada = 2 },
                        icon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                        label = { Text("Perfil", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorActivo,
                            selectedTextColor = colorActivo,
                            unselectedIconColor = colorInactivo,
                            unselectedTextColor = colorInactivo,
                            indicatorColor = Color(0xFFE0F7FA)
                        )
                    )
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier.padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                when (pestañaSeleccionada) {
                    0 -> MisTicketsScreen(
                        vm = misTicketsVm,
                        nombreUsuario = sesionEstado?.nombre ?: "Usuario",
                        onNuevoTicketClick = { pestañaSeleccionada = 1 },
                        onTicketClick = { id -> ticketSeleccionadoId = id }
                    )
                    1 -> NuevoTicketScreen(
                        vm = nuevoTicketVm,
                        onVolver = { pestañaSeleccionada = 0 },
                        onTicketEnviado = {
                            misTicketsVm.agregarTicket(
                                titulo = nuevoTicketVm.titulo.value,
                                ubicacion = nuevoTicketVm.ubicacion.value,
                                prioridad = nuevoTicketVm.prioridad.value,
                                categoria = nuevoTicketVm.categoria.value
                            )
                            pestañaSeleccionada = 0
                        }
                    )
                    2 -> PerfilScreen(
                        nombre = sesionEstado?.nombre ?: "Usuario",
                        correo = sesionEstado?.correo ?: "usuario@campus.test",
                        esModoOscuro = esModoOscuro,
                        onModoOscuroChange = { activado ->
                            sesionVm.cambiarModoOscuro(activado)
                        },
                        onCerrarSesion = onCerrarSesion
                    )
                }
            }
        }
    }
}