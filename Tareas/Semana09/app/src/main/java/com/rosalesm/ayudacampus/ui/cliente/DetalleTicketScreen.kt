package com.rosalesm.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rosalesm.ayudacampus.ui.components.AyudaButton
import com.rosalesm.ayudacampus.viewmodel.ComentarioItem
import com.rosalesm.ayudacampus.viewmodel.TicketItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleTicketScreen(
    ticket: TicketItem,
    onVolver: () -> Unit,
    onEnviarComentario: (String) -> Unit,
    onCalificar: (Int, String) -> Unit
) {
    var nuevoComentario by remember { mutableStateOf("") }
    var calificacion by remember { mutableStateOf(4) }
    var comentarioFinal by remember { mutableStateOf("Atención rápida, gracias.") }

    val colorPrincipal = Color(0xFF00838F)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Cabecera superior
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolver) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Ticket ${ticket.numero}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Tarjeta resumen del Ticket
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = ticket.titulo,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                TagPill(
                                    text = ticket.estado,
                                    bgColor = if (ticket.estado == "Resuelto") Color(0xFFDCFCE7) else Color(0xFFE0F2FE),
                                    textColor = if (ticket.estado == "Resuelto") Color(0xFF16A34A) else Color(0xFF0284C7)
                                )
                                TagPill(
                                    text = ticket.prioridad,
                                    bgColor = Color(0xFFFEE2E2),
                                    textColor = Color(0xFFDC2626)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "📍 ${ticket.ubicacion} · ${ticket.tiempo}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                // Stepper de Seguimiento o Banner Resuelto
                item {
                    if (ticket.estado == "Resuelto") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "✓ Soporte marcó este ticket como resuelto. ¿El problema se solucionó?",
                                fontSize = 13.sp,
                                color = Color(0xFF15803D),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        // Stepper simple
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StepItem(titulo = "Abierto", activo = true, completado = true)
                            StepLine(activo = true)
                            StepItem(titulo = "En proceso", activo = true, completado = false)
                            StepLine(activo = false)
                            StepItem(titulo = "Resuelto", activo = false, completado = false)
                            StepLine(activo = false)
                            StepItem(titulo = "Cerrado", activo = false, completado = false)
                        }
                    }
                }

                // Sección Conversación o Calificación
                if (ticket.estado == "Resuelto") {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Califica la atención",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    for (i in 1..5) {
                                        Icon(
                                            imageVector = if (i <= calificacion) Icons.Default.Star else Icons.Outlined.StarOutline,
                                            contentDescription = null,
                                            tint = Color(0xFFF59E0B),
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clickable { calificacion = i }
                                        )
                                    }
                                }
                                Text(
                                    text = "$calificacion de 5 estrellas",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                                )

                                OutlinedTextField(
                                    value = comentarioFinal,
                                    onValueChange = { comentarioFinal = it },
                                    label = { Text("Comentario final (opcional)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = colorPrincipal,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                                    )
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                AyudaButton(
                                    text = "Confirmar y cerrar",
                                    onClick = { onCalificar(calificacion, comentarioFinal) },
                                    color = colorPrincipal,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedButton(
                                    onClick = { },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626))
                                ) {
                                    Text("Reabrir ticket", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    item {
                        Text(
                            text = "Conversación",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    items(ticket.conversacion) { mensaje ->
                        BurbujaComentario(mensaje = mensaje)
                    }
                }
            }

            // Input para enviar comentario en chat activo
            if (ticket.estado != "Resuelto") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = nuevoComentario,
                        onValueChange = { nuevoComentario = it },
                        placeholder = {
                            Text(
                                "Escribe un comentario...",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorPrincipal,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FloatingActionButton(
                        onClick = {
                            if (nuevoComentario.isNotBlank()) {
                                onEnviarComentario(nuevoComentario)
                                nuevoComentario = ""
                            }
                        },
                        containerColor = colorPrincipal,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Enviar")
                    }
                }
            }
        }
    }
}

@Composable
private fun BurbujaComentario(mensaje: ComentarioItem) {
    val esMio = mensaje.esMio
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (esMio) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (esMio) 16.dp else 0.dp,
                        bottomEnd = if (esMio) 0.dp else 16.dp
                    )
                )
                .background(if (esMio) Color(0xFF00838F) else MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp)
        ) {
            Text(
                text = "${mensaje.autor} · ${mensaje.hora}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (esMio) Color.White.copy(alpha = 0.8f) else Color(0xFF7C3AED)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = mensaje.texto,
                fontSize = 13.sp,
                color = if (esMio) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StepItem(titulo: String, activo: Boolean, completado: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(
                    when {
                        completado -> Color(0xFF16A34A)
                        activo -> Color(0xFF00838F)
                        else -> MaterialTheme.colorScheme.outline
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (completado) {
                Text("✓", color = Color.White, fontSize = 11.sp)
            }
        }
        Text(
            text = titulo,
            fontSize = 10.sp,
            color = if (activo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            fontWeight = if (activo) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun StepLine(activo: Boolean) {
    Box(
        modifier = Modifier
            .width(24.dp)
            .height(2.dp)
            .background(if (activo) Color(0xFF00838F) else MaterialTheme.colorScheme.outline)
    )
}

@Composable
private fun TagPill(text: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = textColor)
    }
}