package com.rosalesm.ayudacampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rosalesm.ayudacampus.model.Rol
import com.rosalesm.ayudacampus.ui.cliente.*
import com.rosalesm.ayudacampus.ui.soporte.MainSoporteScreen
import com.rosalesm.ayudacampus.viewmodel.AyudaViewModelFactory
import com.rosalesm.ayudacampus.viewmodel.MisTicketsViewModel
import com.rosalesm.ayudacampus.viewmodel.NuevoTicketViewModel
import com.rosalesm.ayudacampus.viewmodel.SesionViewModel

@Composable
fun NavAyuda(
    navController: NavHostController,
    sesionViewModel: SesionViewModel,
    factory: AyudaViewModelFactory
) {
    NavHost(
        navController = navController,
        startDestination = Ruta.Splash.path
    ) {
        composable(Ruta.Splash.path) {
            SplashScreen(
                onComenzarClick = { navController.navigate(Ruta.Registro.path) },
                onYaTengoCuentaClick = { navController.navigate(Ruta.Login.path) }
            )
        }

        composable(Ruta.Login.path) {
            LoginScreen(
                vm = sesionViewModel,
                onLoginExitoso = { rol ->
                    val destino = if (rol == Rol.ADMIN) "main_soporte" else Ruta.Main.path
                    navController.navigate(destino) {
                        popUpTo(Ruta.Login.path) { inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(Ruta.Registro.path) }
            )
        }

        composable(Ruta.Registro.path) {
            RegistroScreen(
                vm = sesionViewModel,
                onRegistroCompletado = {
                    navController.navigate(Ruta.Login.path) {
                        popUpTo(Ruta.Registro.path) { inclusive = true }
                    }
                },
                onIrALogin = { navController.navigate(Ruta.Login.path) }
            )
        }

        composable(Ruta.Main.path) {
            val misTicketsVm: MisTicketsViewModel = viewModel(factory = factory)
            val nuevoTicketVm: NuevoTicketViewModel = viewModel(factory = factory)

            MainClienteScreen(
                misTicketsVm = misTicketsVm,
                nuevoTicketVm = nuevoTicketVm,
                sesionVm = sesionViewModel,
                onCerrarSesion = {
                    navController.navigate(Ruta.Login.path) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("main_soporte") {
            MainSoporteScreen(
                sesionVm = sesionViewModel,
                onCerrarSesion = {
                    navController.navigate(Ruta.Login.path) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}