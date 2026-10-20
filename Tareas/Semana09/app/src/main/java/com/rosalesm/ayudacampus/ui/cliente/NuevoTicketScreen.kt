package com.rosalesm.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.rosalesm.ayudacampus.ui.components.AyudaButton
import com.rosalesm.ayudacampus.viewmodel.NuevoTicketViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NuevoTicketScreen(
    vm: NuevoTicketViewModel,
    onVolver: () -> Unit,
    onTicketEnviado: () -> Unit
) {
    val categoria by vm.categoria.collectAsState()
    val titulo by vm.titulo.collectAsState()
    val ubicacion by vm.ubicacion.collectAsState()
    val prioridad by vm.prioridad.collectAsState()
    val descripcion by vm.descripcion.collectAsState()

    val colorPrincipal = Color(0xFF00838F)

    val categoriasList = listOf(
        Triple("Hardware", Icons.Outlined.Computer, "Hardware"),
        Triple("Software", Icons.Outlined.Code, "Software"),
        Triple("Red / Wi-Fi", Icons.Outlined.Wifi, "Red / Wi-Fi"),
        Triple("Cuenta", Icons.Outlined.VpnKey, "Cuenta"),
        Triple("Impresión", Icons.Outlined.Print, "Impresión")
    )

    val prioridadesList = listOf("Baja", "Media", "Alta")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Barra Superior
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolver) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Atrás",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Nuevo ticket",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sección Categoría
            Text(
                text = "Categoría",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categoriasList.forEach { (nombre, icon, id) ->
                    val sel = id == categoria
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (sel) colorPrincipal else MaterialTheme.colorScheme.surface)
                            .border(
                                width = 1.dp,
                                color = if (sel) colorPrincipal else MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { vm.onCategoriaChange(id) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (sel) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = nombre,
                                fontSize = 13.sp,
                                color = if (sel) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (sel) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Campo Título
            OutlinedTextField(
                value = titulo,
                onValueChange = { vm.onTituloChange(it) },
                label = { Text("Título") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorPrincipal,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo Ubicación
            OutlinedTextField(
                value = ubicacion,
                onValueChange = { vm.onUbicacionChange(it) },
                label = { Text("Ubicación") },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null, tint = colorPrincipal)
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorPrincipal,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Sección Prioridad
            Text(
                text = "Prioridad",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                prioridadesList.forEach { p ->
                    val sel = p == prioridad
                    val colorBoton = if (p == "Alta") Color(0xFFDC2626) else colorPrincipal

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (sel) colorBoton else MaterialTheme.colorScheme.surface)
                            .border(
                                width = 1.dp,
                                color = if (sel) colorBoton else MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { vm.onPrioridadChange(p) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = p,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (sel) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Campo Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { vm.onDescripcionChange(it) },
                label = { Text("Descripción") },
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorPrincipal,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Caja de tiempo estimado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0F7FA))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = colorPrincipal,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tiempo de respuesta estimado: 4 horas",
                        fontSize = 13.sp,
                        color = colorPrincipal,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Enviar ticket
            AyudaButton(
                text = "Enviar ticket",
                onClick = onTicketEnviado,
                color = colorPrincipal,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}