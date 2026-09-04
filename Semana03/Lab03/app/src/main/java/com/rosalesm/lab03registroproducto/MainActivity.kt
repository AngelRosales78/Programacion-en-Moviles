package com.rosalesm.lab03registroproducto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rosalesm.lab03registroproducto.ui.theme.Lab03RegistroProductoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab03RegistroProductoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaRegistro(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PantallaRegistro(modifier: Modifier = Modifier) {
    // Estados principales de la pantalla
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var mostrarResumen by remember { mutableStateOf(false) }

    // Estado para gestionar errores de validación
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Encabezado principal con jerarquía tipográfica
        Text(
            text = "Nuevo producto",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Completa los datos y presiona Agregar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Campo: Nombre del producto
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                if (mensajeError.isNotEmpty()) mensajeError = ""
            },
            label = { Text("Nombre del producto") },
            isError = mensajeError.isNotEmpty() && nombre.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Fila compartida para Precio y Cantidad
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = precio,
                onValueChange = {
                    precio = it
                    if (mensajeError.isNotEmpty()) mensajeError = ""
                },
                label = { Text("Precio (S/)") },
                isError = mensajeError.isNotEmpty() && (precio.isBlank() || precio.toDoubleOrNull() == null),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedTextField(
                value = cantidad,
                onValueChange = {
                    cantidad = it
                    if (mensajeError.isNotEmpty()) mensajeError = ""
                },
                label = { Text("Cantidad") },
                isError = mensajeError.isNotEmpty() && (cantidad.isBlank() || cantidad.toIntOrNull() == null),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón de acción principal
        Button(
            onClick = {
                val precioValido = precio.toDoubleOrNull()
                val cantidadValida = cantidad.toIntOrNull()

                if (nombre.isBlank() || precio.isBlank() || cantidad.isBlank()) {
                    mensajeError = "Por favor, completa todos los campos marcados."
                    mostrarResumen = false
                } else if (precioValido == null || cantidadValida == null) {
                    mensajeError = "Precio y cantidad deben ser números válidos."
                    mostrarResumen = false
                } else {
                    mensajeError = ""
                    mostrarResumen = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AGREGAR PRODUCTO")
        }

        // Opción para limpiar formulario de manera secundaria
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = {
                    nombre = ""
                    precio = ""
                    cantidad = ""
                    mostrarResumen = false
                    mensajeError = ""
                }
            ) {
                Text("Limpiar campos", color = MaterialTheme.colorScheme.secondary)
            }
        }

        // Mensaje de alerta en rojo cuando falla la validación
        if (mensajeError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        // Card con el resumen de la compra
        if (mostrarResumen) {
            val precioNum = precio.toDoubleOrNull() ?: 0.0
            val cantidadNum = cantidad.toIntOrNull() ?: 0
            val importeTotal = precioNum * cantidadNum

            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = nombre, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Precio: S/ " + String.format("%.2f", precioNum))
                    Text(text = "Cantidad: $cantidadNum")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Importe total: S/ " + String.format("%.2f", importeTotal),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "✓ Producto registrado correctamente",
                color = Color(0xFF2E7D32),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}