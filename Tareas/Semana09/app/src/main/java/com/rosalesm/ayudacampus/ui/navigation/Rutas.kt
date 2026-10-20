package com.rosalesm.ayudacampus.ui.navigation

sealed class Ruta(val path: String) {
    object Splash : Ruta("splash")
    object Login : Ruta("login")
    object Registro : Ruta("registro")
    object Main : Ruta("main")
}