package com.tecsup.campusgps.util

import java.util.Locale

// Menos de 1000: "350 m". Desde 1000: "1.5 km"
fun formatearDistancia(metros: Float): String =
    if (metros < 1000f) {
        "${metros.toInt()} m"
    } else {
        String.format(Locale.US, "%.1f km", metros / 1000f)
    }
