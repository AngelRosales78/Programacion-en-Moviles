package com.rosalesm.lab04carritotecsup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCarrito() {
    var nombreInput by remember { mutableStateOf("") }
    var precioInput by remember { mutableStateOf("") }
    var cantidadInput by remember { mutableStateOf("") }

    val productos = remember { mutableStateListOf<Producto>() }

    // Cálculo dinámico de totales
    val subtotalGeneral = productos.sumOf { it.subtotal }
    val igv = subtotalGeneral * 0.18
    val totalGeneral = subtotalGeneral + igv

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Carrito TECSUP",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // --- Formulario de Entrada ---
            OutlinedTextField(
                value = nombreInput,
                onValueChange = { nombreInput = it },
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = precioInput,
                    onValueChange = { precioInput = it },
                    label = { Text("Precio (S/)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = cantidadInput,
                    onValueChange = { cantidadInput = it },
                    label = { Text("Cantidad") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val precio = precioInput.toDoubleOrNull()
                    val cantidad = cantidadInput.toIntOrNull()

                    if (nombreInput.isNotBlank() && precio != null && cantidad != null) {
                        productos.add(Producto(nombreInput, precio, cantidad))
                        nombreInput = ""
                        precioInput = ""
                        cantidadInput = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar al Carrito")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Lista o Estado Vacío ---
            if (productos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay productos en el carrito",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productos) { producto ->
                        TarjetaProducto(
                            producto = producto,
                            onEliminar = { productos.remove(producto) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // --- Panel de Totales ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal:")
                            Text(String.format("S/ %.2f", subtotalGeneral))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("IGV (18%):")
                            Text(String.format("S/ %.2f", igv))
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total a Pagar:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = String.format("S/ %.2f", totalGeneral),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}