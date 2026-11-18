package com.tecsup.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.UbicacionUi

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modifier: Modifier = Modifier,
    masCercanoId: Int? = null
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    // Paso 3: al llegar la ubicación, la cámara viaja a tu posición (zoom 16)
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val p = estado.posicion
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(LatLng(p.latitud, p.longitud), 16f),
                1000
            )
        }
    }

    // Paso 6: al seleccionar un lugar, la cámara viaja hasta él (zoom 17)
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
        if (lugar != null) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(LatLng(lugar.latitud, lugar.longitud), 17f),
                1000
            )
        }
    }

    GoogleMap(modifier = modifier, cameraPositionState = camara) {
        // Tu posición: marcador rojo por defecto
        if (estado is UbicacionUi.Lista) {
            Marker(
                state = rememberUpdatedMarkerState(
                    position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
                ),
                title = "Estás aquí"
            )
        }

        // Paso 4 y 7: un marcador por lugar, azul; el más cercano en verde
        lugares.forEach { item ->
            val lugar = item.lugar
            key(lugar.id) {
                val estadoMarcador = rememberUpdatedMarkerState(
                    position = LatLng(lugar.latitud, lugar.longitud)
                )
                LaunchedEffect(seleccionado) {
                    if (seleccionado == lugar.id) estadoMarcador.showInfoWindow()
                }
                val tono = if (lugar.id == masCercanoId) {
                    BitmapDescriptorFactory.HUE_GREEN
                } else {
                    BitmapDescriptorFactory.HUE_AZURE
                }
                Marker(
                    state = estadoMarcador,
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(tono)
                )
            }
        }
    }
}
