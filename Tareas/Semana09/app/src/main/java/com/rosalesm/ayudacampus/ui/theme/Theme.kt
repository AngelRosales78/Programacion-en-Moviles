package com.rosalesm.ayudacampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.rosalesm.ayudacampus.model.Rol

@Composable
fun AyudaCampusTheme(darkTheme: Boolean=isSystemInDarkTheme(),rol: Rol=Rol.CLIENTE,content: @Composable () -> Unit) {
    val soporte=rol==Rol.ADMIN
    val scheme=if(darkTheme) darkColorScheme(
        primary=if(soporte) Color(0xFFC4B5FD) else Color(0xFF67E8F9),
        primaryContainer=if(soporte) Color(0xFF4C1D95) else Color(0xFF164E63),
        background=Color(0xFF10151E),surface=Color(0xFF1A2230)
    ) else lightColorScheme(
        primary=if(soporte) Color(0xFF7C3AED) else Color(0xFF0891B2),
        primaryContainer=if(soporte) Color(0xFFEDE4FF) else Color(0xFFCFFAFE),
        background=if(soporte) Color(0xFFFAF8FF) else Color(0xFFF7FDFE),surface=Color.White,
        onBackground=Color(0xFF1E293B),onSurface=Color(0xFF1E293B)
    )
    MaterialTheme(colorScheme=scheme,typography=Typography,content=content)
}
