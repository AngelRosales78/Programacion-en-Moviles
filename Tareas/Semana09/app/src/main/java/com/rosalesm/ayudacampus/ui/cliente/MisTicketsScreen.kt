package com.rosalesm.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rosalesm.ayudacampus.viewmodel.MisTicketsViewModel
import com.rosalesm.ayudacampus.viewmodel.TicketItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisTicketsScreen(
    vm: MisTicketsViewModel,
    nombreUsuario: String,
    onNuevoTicketClick: () -> Unit,
    onTicketClick: (String) -> Unit
) {
    val busqueda by vm.busqueda.collectAsState()
    val filtroSeleccionado by vm.filtroSeleccionado.collectAsState()
    val tickets by vm.tickets.collectAsState()

    val primerNombre = nombreUsuario.split(" ").firstOrNull() ?: "Usuario"
    val filtros = listOf("Todos", "Abiertos", "En proceso", "Resueltos")

    val ticketsFiltrados = tickets.filter { ticket ->
        val coincideBusqueda = ticket.titulo.contains(busqueda, ignoreCase = true) ||
                ticket.numero.contains(busqueda, ignoreCase = true)
        val coincideFiltro = when (filtroSeleccionado) {
            "Abiertos" -> ticket.estado == "Abierto"
            "En proceso" -> ticket.estado == "En proceso"
            "Resueltos" -> ticket.estado == "Resuelto"
            else -> true
        }
        coincideBusqueda && coincideFiltro
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevoTicketClick,
                containerColor = Color(0xFF00838F),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nuevo Ticket",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Cabecera Dinámica
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Mis tickets",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Hola, $primerNombre · 3 abiertos",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notificaciones",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Buscador
            OutlinedTextField(
                value = busqueda,
                onValueChange = { vm.onBusquedaChange(it) },
                placeholder = {
                    Text(
                        "Buscar por título o número...",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Chips de Filtro
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtros) { filtro ->
                    val seleccionado = filtro == filtroSeleccionado
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (seleccionado) Color(0xFF00838F) else Color.Transparent
                            )
                            .border(
                                width = 1.dp,
                                color = if (seleccionado) Color(0xFF00838F) else MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { vm.onFiltroChange(filtro) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = filtro,
                            fontSize = 13.sp,
                            fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (seleccionado) Color.White else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Tickets
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(ticketsFiltrados) { ticket ->
                    TicketCard(ticket = ticket, onClick = { onTicketClick(ticket.id) })
                }
            }
        }
    }
}

@Composable
private fun TicketCard(ticket: TicketItem, onClick: () -> Unit) {
    val colorBordeIzquierdo = when (ticket.prioridad) {
        "Alta" -> Color(0xFFDC2626)
        "Media" -> Color(0xFFD97706)
        else -> Color(0xFF64748B)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(colorBordeIzquierdo)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE0F7FA), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        val icon = when (ticket.icono) {
                            "wifi" -> Icons.Outlined.Wifi
                            "computer" -> Icons.Outlined.Computer
                            else -> Icons.Outlined.Print
                        }
                        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF00838F), modifier = Modifier.size(20.dp))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = ticket.titulo,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${ticket.numero} · ${ticket.ubicacion}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        val bgEstado = when (ticket.estado) {
                            "En proceso" -> Color(0xFFE0F2FE)
                            "Abierto" -> Color(0xFFFEF3C7)
                            else -> Color(0xFFDCFCE7)
                        }
                        val txtEstado = when (ticket.estado) {
                            "En proceso" -> Color(0xFF0284C7)
                            "Abierto" -> Color(0xFFD97706)
                            else -> Color(0xFF16A34A)
                        }
                        TagPill(text = ticket.estado, bgColor = bgEstado, textColor = txtEstado)

                        val bgPrioridad = when (ticket.prioridad) {
                            "Alta" -> Color(0xFFFEE2E2)
                            "Media" -> Color(0xFFFFEDD5)
                            else -> Color(0xFFF1F5F9)
                        }
                        val txtPrioridad = when (ticket.prioridad) {
                            "Alta" -> Color(0xFFDC2626)
                            "Media" -> Color(0xFFEA580C)
                            else -> Color(0xFF64748B)
                        }
                        TagPill(text = ticket.prioridad, bgColor = bgPrioridad, textColor = txtPrioridad)
                    }

                    Text(
                        text = ticket.tiempo,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TagPill(text: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = textColor)
    }
}