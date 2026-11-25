package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.notificacion.Notificador
import com.tecsup.avisoscampus.ui.theme.BordeTarjeta
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            // TODO Paso 8: si no se mostró, avisar al ViewModel
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus", fontWeight = FontWeight.Bold) },
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
            TarjetaPermiso(vm)
            TarjetaAvisos(vm)
            TarjetaRecordatorio(vm)
            TarjetaReceptor(vm)
            Tarjeta(titulo = "Historial") { } // TODO Paso 7: HistorialAvisos
        }
    }
}

// ---------- Permiso de notificaciones (pasos 2 y 8) ----------
@Composable
private fun TarjetaPermiso(vm: AvisosViewModel) {
    Tarjeta(titulo = "Permiso de notificaciones") {
        // TODO Paso 2: etiqueta de estado y botón "Permitir notificaciones"
    }
}

// ---------- Avisos (paso 3) ----------
@Composable
private fun TarjetaAvisos(vm: AvisosViewModel) {
    Tarjeta(titulo = "Avisos") {
        // TODO Paso 3: botón "Mostrar aviso"
    }
}

// ---------- Recordatorio (paso 5) ----------
@Composable
private fun TarjetaRecordatorio(vm: AvisosViewModel) {
    Tarjeta(titulo = "Recordatorio") {
        // TODO Paso 5: botones y cuenta regresiva
    }
}

// ---------- Receptor de anuncios (paso 6) ----------
@Composable
private fun TarjetaReceptor(vm: AvisosViewModel) {
    Tarjeta(titulo = "Receptor de anuncios") {
        // TODO Paso 6: Switch "Escuchar el modo avión"
    }
}

// ---------- Piezas reutilizables ----------
@Composable
fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                titulo,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            contenido()
        }
    }
}

@Composable
fun Etiqueta(texto: String, fondo: Color, color: Color) {
    Text(
        texto,
        modifier = Modifier
            .background(fondo, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        color = color,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelMedium
    )
}
