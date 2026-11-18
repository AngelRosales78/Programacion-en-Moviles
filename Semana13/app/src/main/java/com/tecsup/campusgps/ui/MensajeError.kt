package com.tecsup.campusgps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.TipoError
import com.tecsup.campusgps.model.UbicacionUi

// Recuadro de error y, debajo, el botón según el tipo
@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onAbrirAjustes: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = error.mensaje,
            color = MaterialTheme.colorScheme.onErrorContainer,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.errorContainer,
                    RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        )
        Spacer(Modifier.height(16.dp))
        when (error.tipo) {
            TipoError.SIN_PERMISO ->
                OutlinedButton(onClick = onAbrirAjustes) { Text("Abrir ajustes") }
            TipoError.GPS_APAGADO, TipoError.OTRO ->
                OutlinedButton(onClick = onReintentar) { Text("Reintentar") }
            TipoError.SIN_PLAY_SERVICES -> Unit // sin botón: no hay nada que reintentar
        }
    }
}
