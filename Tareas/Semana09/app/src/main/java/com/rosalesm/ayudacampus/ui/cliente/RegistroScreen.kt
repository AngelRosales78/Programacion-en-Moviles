package com.rosalesm.ayudacampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.model.Rol
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.SesionViewModel
@Composable
fun RegistroScreen(vm: SesionViewModel,onRegistroCompletado: () -> Unit,onIrALogin: () -> Unit) {
    var nombre by rememberSaveable { mutableStateOf("") }; var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }; var repetir by rememberSaveable { mutableStateOf("") }
    val error by vm.error.collectAsState(); val cargando by vm.cargando.collectAsState()
    var errorLocal by remember { mutableStateOf<String?>(null) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
        Cabecera("Crear cuenta","Estudiante · Solo datos ficticios",onIrALogin)
        AyudaTextField(nombre,{ nombre=it },"Nombre")
        AyudaTextField(correo,{ correo=it },"Correo")
        AyudaTextField(clave,{ clave=it },"Contraseña",isPassword=true)
        AyudaTextField(repetir,{ repetir=it },"Confirmar contraseña",isPassword=true)
        MensajeError(errorLocal ?: error)
        AyudaButton("Registrarme",{
            if(clave!=repetir) errorLocal="Las contraseñas no coinciden"
            else { errorLocal=null; vm.registrarUsuario(nombre,correo,clave,Rol.CLIENTE,onRegistroCompletado) }
        },enabled=!cargando)
    }
}
