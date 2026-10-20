package com.rosalesm.ayudacampus.ui.soporte

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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TicketSoporteItem(
    val id: String,
    val numero: String,
    val usuario: String,
    val titulo: String,
    val estado: String,
    val prioridad: String,
    val tiempo: String,
    val icono: ImageVector
)

@Composable
fun BandejaScreen(
    onTicketClick: (String) -> Unit = {}
) {
    // Se inicia por defecto en "Todos"
    var filtroSeleccionado by remember { mutableStateOf("Todos") }
    val filtros = listOf("Todos", "Abiertos", "En proceso", "Resueltos")

    val listaTickets = remember {
        listOf(
            TicketSoporteItem(
                id = "1",
                numero = "#0014",
                usuario = "Ana T.",
                titulo = "Servidor del aula 305 caído",
                estado = "Abierto",
                prioridad = "Alta",
                tiempo = "Hace 20 min",
                icono = Icons.Outlined.Computer
            ),
            TicketSoporteItem(
                id = "2",
                numero = "#0012",
                usuario = "Juan P.",
                titulo = "Sin Wi-Fi en el laboratorio 3",
                estado = "En proceso",
                prioridad = "Alta",
                tiempo = "Hace 2 h",
                icono = Icons.Outlined.Wifi
            ),
            TicketSoporteItem(
                id = "3",
                numero = "#0013",
                usuario = "Luis R.",
                titulo = "Instalar Android Studio",
                estado = "Abierto",
                prioridad = "Media",
                tiempo = "Hace 3 h",
                icono = Icons.Outlined.Code
            ),
            TicketSoporteItem(
                id = "4",
                numero = "#0010",
                usuario = "Marta G.",
                titulo = "Olvidé mi contraseña del aula virtual",
                estado = "Abierto",
                prioridad = "Baja",
                tiempo = "Ayer",
                icono = Icons.Outlined.VpnKey
            )
        )
    }

    val ticketsFiltrados = listaTickets.filter { ticket ->
        when (filtroSeleccionado) {
            "Abiertos" -> ticket.estado == "Abierto"
            "En proceso" -> ticket.estado == "En proceso"
            "Resueltos" -> ticket.estado == "Resuelto"
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Tickets",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtros) { filtro ->
                val seleccionado = filtro == filtroSeleccionado
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (seleccionado) Color(0xFF7C3AED) else MaterialTheme.colorScheme.surface
                        )
                        .border(
                            width = 1.dp,
                            color = if (seleccionado) Color(0xFF7C3AED) else MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { filtroSeleccionado = filtro }
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

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(ticketsFiltrados) { item ->
                TicketSoporteCard(item = item, onClick = { onTicketClick(item.numero) })
            }
        }
    }
}

@Composable
private fun TicketSoporteCard(item: TicketSoporteItem, onClick: () -> Unit) {
    val colorBordeLateral = when (item.prioridad) {
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
                    .background(colorBordeLateral)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFEDE4FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icono,
                            contentDescription = null,
                            tint = Color(0xFF7C3AED),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = item.titulo,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${item.numero} · ${item.usuario}",
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
                        val bgEstado = when (item.estado) {
                            "En proceso" -> Color(0xFFE0F2FE)
                            "Abierto" -> Color(0xFFFEF3C7)
                            else -> Color(0xFFDCFCE7)
                        }
                        val txtEstado = when (item.estado) {
                            "En proceso" -> Color(0xFF0284C7)
                            "Abierto" -> Color(0xFFD97706)
                            else -> Color(0xFF16A34A)
                        }
                        TagPill(text = item.estado, bgColor = bgEstado, textColor = txtEstado)

                        val bgPrioridad = when (item.prioridad) {
                            "Alta" -> Color(0xFFFEE2E2)
                            "Media" -> Color(0xFFFFEDD5)
                            else -> Color(0xFFF1F5F9)
                        }
                        val txtPrioridad = when (item.prioridad) {
                            "Alta" -> Color(0xFFDC2626)
                            "Media" -> Color(0xFFEA580C)
                            else -> Color(0xFF64748B)
                        }
                        TagPill(text = item.prioridad, bgColor = bgPrioridad, textColor = txtPrioridad)
                    }

                    Text(
                        text = item.tiempo,
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