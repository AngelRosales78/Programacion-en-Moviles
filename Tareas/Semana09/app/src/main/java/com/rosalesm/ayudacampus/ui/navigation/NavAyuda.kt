package com.rosalesm.ayudacampus.ui.navigation
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.rosalesm.ayudacampus.model.Rol
import com.rosalesm.ayudacampus.ui.cliente.*
import com.rosalesm.ayudacampus.ui.soporte.MainSoporteScreen
import com.rosalesm.ayudacampus.viewmodel.*
@Composable
fun NavAyuda(navController: NavHostController,sesionViewModel: SesionViewModel,factory: AyudaViewModelFactory) {
    val lista by sesionViewModel.lista.collectAsState()
    val usuario by sesionViewModel.sesion.collectAsState()
    if(!lista) { Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center) { CircularProgressIndicator() }; return }
    val u=usuario
    if(u!=null) key(u.id,u.rol) {
        if(u.rol==Rol.ADMIN.name) MainSoporteScreen(u,sesionViewModel,factory)
        else MainClienteScreen(u,sesionViewModel,factory)
    } else key("sin-sesion") {
        NavHost(navController,startDestination="splash") {
            composable("splash") { SplashScreen(onComenzarClick={ navController.navigate("registro") },onYaTengoCuentaClick={ navController.navigate("login") }) }
            composable("login") { LoginScreen(sesionViewModel,onLoginExitoso={},onIrARegistro={ sesionViewModel.limpiarError(); navController.navigate("registro") }) }
            composable("registro") { RegistroScreen(sesionViewModel,onRegistroCompletado={ navController.navigate("login") { popUpTo("splash") } },onIrALogin={ sesionViewModel.limpiarError(); navController.navigate("login") { popUpTo("splash") } }) }
        }
    }
}
