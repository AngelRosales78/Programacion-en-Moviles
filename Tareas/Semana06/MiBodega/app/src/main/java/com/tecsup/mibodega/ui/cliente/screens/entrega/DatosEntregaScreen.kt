package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatosEntregaScreen(
    onVolver: () -> Unit,
    onConfirmarPedido: () -> Unit
) {
    var direccion by remember { mutableStateOf("Av. Los Olivos 123") }
    var referencia by remember { mutableStateOf("Frente al parque") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de Entrega") },
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
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Confirma tu dirección",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                CampoTexto(
                    etiqueta = "Dirección de entrega",
                    valor = direccion,
                    onValorCambia = { direccion = it },
                    placeholder = "Ingresa tu dirección"
                )
                Spacer(modifier = Modifier.height(12.dp))
                CampoTexto(
                    etiqueta = "Referencia",
                    valor = referencia,
                    onValorCambia = { referencia = it },
                    placeholder = "Referencia cercana"
                )
            }

            BotonPrimario(
                texto = "Confirmar pedido",
                onClick = onConfirmarPedido
            )
        }
    }
}