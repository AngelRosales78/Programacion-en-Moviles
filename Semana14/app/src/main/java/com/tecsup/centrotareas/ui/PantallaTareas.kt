package com.tecsup.centrotareas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import com.tecsup.centrotareas.ui.theme.BordeTarjeta
import com.tecsup.centrotareas.ui.theme.PistaProgreso
import com.tecsup.centrotareas.ui.theme.VerdeFondo
import com.tecsup.centrotareas.ui.theme.VerdeTexto
import com.tecsup.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TarjetaProgreso(vm)
            TarjetaPrimos(vm)
            TarjetaCargas(vm)
        }
    }
}

// ---------- Tarjeta 1: Tarea con progreso (pasos 1 a 4) ----------
@Composable
private fun TarjetaProgreso(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    var toques by rememberSaveable { mutableIntStateOf(0) }

    Tarjeta(titulo = "Tarea con progreso") {
        Button(onClick = { toques++ }) {
            Text("Tócame: $toques", fontWeight = FontWeight.Bold)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.tareaLarga() }, enabled = !corriendo) {
                Text("Iniciar", fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = { vm.cancelar() },
                enabled = corriendo,
                border = bordeBoton(corriendo),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Cancelar", fontWeight = FontWeight.Bold)
            }
        }
        LinearProgressIndicator(
            progress = { progreso },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            trackColor = PistaProgreso,
            gapSize = 0.dp,
            drawStopIndicator = {}
        )
        Text(
            "${(progreso * 100).toInt()} %",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(mensaje)
    }
}

// ---------- Tarjeta 2: Contar números primos (paso 5) ----------
@Composable
private fun TarjetaPrimos(vm: TareasViewModel) {
    val estado by vm.primos.collectAsState()
    var texto by rememberSaveable { mutableStateOf("2000000") }
    var ultimoN by rememberSaveable { mutableIntStateOf(0) }
    val numero = texto.toIntOrNull()
    val valido = numero != null && numero >= 2
    val calculando = estado == PrimosUi.Calculando

    Tarjeta(titulo = "Contar números primos") {
        OutlinedTextField(
            value = texto,
            onValueChange = { nuevo -> if (nuevo.all { it.isDigit() } && nuevo.length <= 9) texto = nuevo },
            label = { Text("Hasta el número") },
            singleLine = true,
            isError = texto.isNotEmpty() && !valido,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    if (numero != null && numero >= 2) {
                        ultimoN = numero
                        vm.contar(numero)
                    }
                },
                enabled = valido && !calculando
            ) {
                Text("Contar", fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = { vm.cancelarPrimos() },
                enabled = calculando,
                border = bordeBoton(calculando),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Cancelar", fontWeight = FontWeight.Bold)
            }
        }
        when (val e = estado) {
            PrimosUi.Inicial -> Unit
            PrimosUi.Calculando -> Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Text("Calculando...")
            }
            is PrimosUi.Listo -> RecuadroExito {
                Text(
                    "Primos hasta $ultimoN: ${e.cantidad}",
                    fontWeight = FontWeight.Bold,
                    color = VerdeTexto
                )
                Text("Tiempo: ${e.milisegundos} ms", color = VerdeTexto)
            }
            PrimosUi.Cancelado -> RecuadroError("Cancelado")
        }
    }
}

// ---------- Tarjeta 3: Dos cargas (pasos 6 y 7) ----------
@Composable
private fun TarjetaCargas(vm: TareasViewModel) {
    val estado by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()
    val cargando = estado == CargaUi.Cargando

    Tarjeta(titulo = "Dos cargas") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = simularError,
                onCheckedChange = { vm.cambiarSimularError(it) }
            )
            Spacer(Modifier.width(8.dp))
            Text("Simular error")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { vm.cargarUnaPorUna() },
                enabled = !cargando,
                border = bordeBoton(!cargando),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Una por una", fontWeight = FontWeight.Bold)
            }
            Button(onClick = { vm.cargarALaVez() }, enabled = !cargando) {
                Text("A la vez", fontWeight = FontWeight.Bold)
            }
        }
        when (val e = estado) {
            CargaUi.Inicial -> Unit
            CargaUi.Cargando -> CircularProgressIndicator()
            is CargaUi.Exito -> RecuadroExito {
                Text("Modo: ${e.modo}", fontWeight = FontWeight.Bold, color = VerdeTexto)
                Text("Perfil: ${e.perfil}", color = VerdeTexto)
                Text("Pedidos: ${e.pedidos}", color = VerdeTexto)
                Text("Tiempo: ${e.milisegundos} ms", color = VerdeTexto)
            }
            is CargaUi.Error -> {
                RecuadroError(e.mensaje)
                OutlinedButton(
                    onClick = { vm.reintentar() },
                    border = bordeBoton(true),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Reintentar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ---------- Piezas reutilizables ----------
@Composable
private fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BordeTarjeta)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                titulo,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            contenido()
        }
    }
}

@Composable
private fun RecuadroExito(contenido: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(VerdeFondo, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = contenido
    )
}

@Composable
private fun RecuadroError(mensaje: String) {
    Text(
        mensaje,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(12.dp))
            .padding(12.dp),
        color = MaterialTheme.colorScheme.onErrorContainer,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun bordeBoton(habilitado: Boolean) = BorderStroke(
    if (habilitado) 2.dp else 1.dp,
    if (habilitado) MaterialTheme.colorScheme.primary else BordeTarjeta
)
