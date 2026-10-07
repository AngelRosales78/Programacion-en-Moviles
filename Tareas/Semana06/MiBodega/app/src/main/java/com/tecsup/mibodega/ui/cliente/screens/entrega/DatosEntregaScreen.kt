package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Usuario
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.theme.VerdeBodega

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatosEntregaScreen(
    usuario: Usuario?,
    esDelivery: Boolean,
    onVolver: () -> Unit,
    onConfirmarPedido: (direccion: String, referencia: String) -> Unit
) {
    var nombre by remember { mutableStateOf(usuario?.nombre ?: "Juan Pérez") }
    var telefono by remember { mutableStateOf(usuario?.telefono ?: "987 654 321") }
    var direccion by remember { mutableStateOf(usuario?.direccion ?: "Av. Los Olivos 123") }
    var referencia by remember { mutableStateOf(usuario?.referencia ?: "Frente al parque") }

    var errorNombre by remember { mutableStateOf(false) }
    var errorTelefono by remember { mutableStateOf(false) }
    var errorDireccion by remember { mutableStateOf(false) }
    var errorReferencia by remember { mutableStateOf(false) }

    val metodosPago = listOf("Efectivo al entregar", "Yape", "Plin")
    var metodoSeleccionado by remember { mutableStateOf(metodosPago[0]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de entrega", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        errorNombre = false
                    },
                    label = { Text("Nombre") },
                    isError = errorNombre,
                    supportingText = { if (errorNombre) Text("Requerido") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = telefono,
                    onValueChange = {
                        telefono = it
                        errorTelefono = false
                    },
                    label = { Text("Teléfono") },
                    isError = errorTelefono,
                    supportingText = { if (errorTelefono) Text("Requerido") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (esDelivery) {
                    OutlinedTextField(
                        value = direccion,
                        onValueChange = {
                            direccion = it
                            errorDireccion = false
                        },
                        label = { Text("Dirección") },
                        isError = errorDireccion,
                        supportingText = { if (errorDireccion) Text("Requerido para Delivery") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = referencia,
                        onValueChange = {
                            referencia = it
                            errorReferencia = false
                        },
                        label = { Text("Referencia") },
                        isError = errorReferencia,
                        supportingText = { if (errorReferencia) Text("Requerido para Delivery") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "Opción seleccionada: Recojo en tienda (Sin costo de envío)",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Método de pago",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                metodosPago.forEach { opcion ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .selectable(
                                selected = (opcion == metodoSeleccionado),
                                onClick = { metodoSeleccionado = opcion }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (opcion == metodoSeleccionado),
                            onClick = { metodoSeleccionado = opcion },
                            colors = RadioButtonDefaults.colors(selectedColor = VerdeBodega)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = opcion, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                BotonPrimario(
                    texto = "Confirmar pedido",
                    onClick = {
                        val eNom = nombre.isBlank()
                        val eTel = telefono.isBlank()
                        val eDir = esDelivery && direccion.isBlank()
                        val eRef = esDelivery && referencia.isBlank()

                        errorNombre = eNom
                        errorTelefono = eTel
                        errorDireccion = eDir
                        errorReferencia = eRef

                        if (!eNom && !eTel && !eDir && !eRef) {
                            onConfirmarPedido(if (esDelivery) direccion else "Recojo en tienda", if (esDelivery) referencia else "-")
                        }
                    }
                )
            }
        }
    }
}