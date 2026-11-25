package com.tecsup.avisoscampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = AzulAvisos,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = Color(0xFF0B2E4A),
    background = Fondo,
    surface = Fondo,
    surfaceVariant = PistaProgreso,
    outlineVariant = BordeTarjeta,
    errorContainer = ErrorFondo,
    onErrorContainer = ErrorTexto
)

@Composable
fun AvisosCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography(),
        content = content
    )
}
