package com.rosalesm.ayudacampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.model.Rol
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.SesionViewModel
@Composable
fun RegistroScreen(vm: SesionViewModel,onRegistroCompletado: ()->Unit,onIrALogin: ()->Unit) {
    var nombre by rememberSaveable { mutableStateOf("") }; var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }; var repetir by rememberSaveable { mutableStateOf("") }
    val error by vm.error.collectAsState(); val cargando by vm.cargando.collectAsState()
    var errorLocal by remember { mutableStateOf<String?>(null) }
    Surface(Modifier.fillMaxSize(),color=MaterialTheme.colorScheme.surface) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).systemBarsPadding().padding(24.dp),verticalArrangement=Arrangement.spacedBy(14.dp)) {
            Cabecera("Crear cuenta",onVolver=onIrALogin)
            Text("Regístrate en AyudaCampus",style=MaterialTheme.typography.headlineSmall)
            Text("Tus datos aparecerán en tu perfil.",style=MaterialTheme.typography.bodyMedium,color=MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(4.dp))
            AyudaTextField(nombre,{ nombre=it },"Nombre",icono=Icons.Outlined.Person)
            AyudaTextField(correo,{ correo=it },"Correo",icono=Icons.Outlined.Email)
            AyudaTextField(clave,{ clave=it },"Contraseña",isPassword=true,icono=Icons.Outlined.Lock)
            AyudaTextField(repetir,{ repetir=it },"Confirmar contraseña",isPassword=true,icono=Icons.Outlined.Lock)
            MensajeError(errorLocal ?: error)
            AyudaButton(if(cargando) "Registrando…" else "Registrarme",{
                if(clave!=repetir) errorLocal="Las contraseñas no coinciden"
                else { errorLocal=null; vm.registrarUsuario(nombre,correo,clave,Rol.CLIENTE,onRegistroCompletado) }
            },enabled=!cargando)
            TextButton(onClick=onIrALogin,modifier=Modifier.fillMaxWidth()) { Text("¿Ya tienes cuenta?  Inicia sesión") }
        }
    }
}
