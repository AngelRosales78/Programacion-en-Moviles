package com.tecsup.campusgps.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.formatearDistancia
import com.tecsup.campusgps.viewmodel.MapaViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugaresVisibles.collectAsState()
    val categoria by vm.categoria.collectAsState()
    val radio by vm.radio.collectAsState()
    val dentroDelRadio by vm.idsDentroDelRadio.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()

    val contexto = LocalContext.current

    fun tienePermiso(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    // Mejora IA 3: el mapa solo activa el punto azul si el permiso está concedido.
    // Se vuelve a revisar cada vez que cambia el estado (p. ej. al volver de Ajustes).
    var permisoConcedido by remember { mutableStateOf(tienePermiso()) }
    LaunchedEffect(estado) { permisoConcedido = tienePermiso() }

    // Paso 2: el cuadro de permiso lo dibuja Android; nosotros solo lo pedimos
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        permisoConcedido = resultado.values.any { it }
        if (permisoConcedido) vm.buscar() else vm.permisoDenegado()
    }

    fun alPulsar() {
        if (tienePermiso()) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    fun abrirAjustes() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", contexto.packageName, null)
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        contexto.startActivity(intent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        val actual = estado
        if (actual is UbicacionUi.Error) {
            // Paso 8: pantalla de error (sin mapa): botón, mensaje y acción
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(12.dp))
                Button(onClick = { alPulsar() }) { Text("Obtener mi ubicación") }
                Spacer(Modifier.height(16.dp))
                MensajeError(
                    error = actual,
                    onAbrirAjustes = { abrirAjustes() },
                    onReintentar = { alPulsar() }
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Paso 3: el mapa ocupa el espacio libre
                MapaUbicacion(
                    estado = actual,
                    lugares = lugares,
                    seleccionado = seleccionado,
                    masCercanoId = masCercano?.id,
                    radio = radio,
                    permisoConcedido = permisoConcedido,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                // Mejora IA 1: chips de categoría
                FiltroCategorias(
                    categorias = vm.categorias,
                    seleccionada = categoria,
                    onSeleccion = { vm.filtrarCategoria(it) },
                    modifier = Modifier.fillMaxWidth()
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = { alPulsar() },
                        enabled = actual !is UbicacionUi.Buscando
                    ) { Text("Obtener mi ubicación") }

                    when (actual) {
                        UbicacionUi.Inicial -> Text("Pulsa el botón para empezar")
                        UbicacionUi.Buscando -> Text("Buscando tu ubicación…")
                        is UbicacionUi.Lista -> Text(
                            text = String.format(
                                Locale.US, "Latitud: %.4f   Longitud: %.4f",
                                actual.posicion.latitud, actual.posicion.longitud
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        else -> Unit
                    }

                    // Paso 7: botón y texto del más cercano
                    if (actual is UbicacionUi.Lista) {
                        OutlinedButton(
                            onClick = { masCercano?.let { vm.seleccionar(it.id) } },
                            enabled = masCercano != null
                        ) { Text("Ir al más cercano") }

                        // Mejora IA 2: selector de radio
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Radio:", style = MaterialTheme.typography.bodyMedium)
                            vm.radios.forEach { r ->
                                FilterChip(
                                    selected = radio == r,
                                    onClick = { vm.cambiarRadio(r) },
                                    label = { Text("$r m") }
                                )
                            }
                        }

                        val cercano = masCercano
                        val metros = lugares.firstOrNull { it.lugar.id == cercano?.id }?.metros
                        if (cercano != null && metros != null) {
                            Text(
                                buildAnnotatedString {
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Más cercano: ")
                                    }
                                    append("${cercano.nombre} (${formatearDistancia(metros)})")
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Paso 6: lista con altura fija
                ListaLugares(
                    lugares = lugares,
                    seleccionado = seleccionado,
                    masCercanoId = masCercano?.id,
                    dentroDelRadio = dentroDelRadio,
                    onClick = { vm.seleccionar(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
        }
    }
}
