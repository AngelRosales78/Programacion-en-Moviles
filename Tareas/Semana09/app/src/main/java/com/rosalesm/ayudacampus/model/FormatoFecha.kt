package com.rosalesm.ayudacampus.model
fun fechaRelativa(fecha: Long,ahora: Long=System.currentTimeMillis()): String {
    val minutos=((ahora-fecha).coerceAtLeast(0)/60_000)
    return when { minutos<1->"Ahora"; minutos<60->"Hace $minutos min"; minutos<1440->"Hace ${minutos/60} h"; minutos<2880->"Ayer"; else->"Hace ${minutos/1440} d" }
}
