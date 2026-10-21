package com.rosalesm.ayudacampus.ui.theme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
private fun texto(tamano: Int, alto: Int, peso: FontWeight=FontWeight.Normal)=TextStyle(fontFamily=FontFamily.SansSerif,fontSize=tamano.sp,lineHeight=alto.sp,fontWeight=peso,letterSpacing=0.sp)
val Typography=Typography(
    headlineLarge=texto(28,34,FontWeight.Bold),headlineMedium=texto(25,30,FontWeight.Bold),headlineSmall=texto(22,28,FontWeight.Bold),
    titleLarge=texto(18,24,FontWeight.Bold),titleMedium=texto(14,20,FontWeight.SemiBold),titleSmall=texto(13,18,FontWeight.SemiBold),
    bodyLarge=texto(14,20),bodyMedium=texto(12,18),bodySmall=texto(11,16),
    labelLarge=texto(13,18,FontWeight.SemiBold),labelMedium=texto(11,16,FontWeight.Medium),labelSmall=texto(10,14,FontWeight.Medium)
)
