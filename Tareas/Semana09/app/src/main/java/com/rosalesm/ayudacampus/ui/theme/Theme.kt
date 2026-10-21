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
        secondaryContainer=if(soporte) Color(0xFF4C1D95) else Color(0xFF164E63),
        surfaceContainer=Color(0xFF1A2230),surfaceContainerHighest=Color(0xFF263244),
        tertiary=Color(0xFF86EFAC),tertiaryContainer=Color(0xFF14532D),onTertiaryContainer=Color(0xFFDCFCE7),
        background=Color(0xFF10151E),surface=Color(0xFF1A2230)
    ) else lightColorScheme(
        primary=if(soporte) Color(0xFF7C3AED) else Color(0xFF0891B2),
        primaryContainer=if(soporte) Color(0xFFEDE4FF) else Color(0xFFCFFAFE),
        secondaryContainer=if(soporte) Color(0xFFEDE4FF) else Color(0xFFCFFAFE),
        surfaceContainer=Color.White,surfaceContainerHighest=Color.White,outline=Color(0xFFE1E5EA),outlineVariant=Color(0xFFE1E5EA),onSurfaceVariant=Color(0xFF77808C),
        background=if(soporte) Color(0xFFFAF8FF) else Color(0xFFF6FCFE),surface=Color.White,
        tertiary=Color(0xFF16A34A),tertiaryContainer=Color(0xFFDCFCE7),onTertiaryContainer=Color(0xFF14532D),
        onBackground=Color(0xFF1E293B),onSurface=Color(0xFF1E293B)
    )
    MaterialTheme(colorScheme=scheme,typography=Typography,content=content)
}
