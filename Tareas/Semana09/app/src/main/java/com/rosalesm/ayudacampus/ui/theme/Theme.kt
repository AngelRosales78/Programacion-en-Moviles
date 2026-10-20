package com.rosalesm.ayudacampus.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Tema Claro
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00838F),
    background = Color(0xFFFAF8FF),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1E293B), // Letras en tema claro (Casi negro/Oscuro)
    onSurface = Color(0xFF1E293B)      // Letras sobre tarjetas/superficies
)

// Tema Oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00838F),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onBackground = Color(0xFFFFFFFF), // Letras en tema oscuro (Blanco)
    onSurface = Color(0xFFFFFFFF)      // Letras sobre tarjetas (Blanco)
)

@Composable
fun AyudaCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}