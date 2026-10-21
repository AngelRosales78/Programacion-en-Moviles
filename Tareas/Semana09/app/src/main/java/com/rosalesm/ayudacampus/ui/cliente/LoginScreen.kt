package com.rosalesm.ayudacampus.ui.cliente
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.model.Rol
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.ui.theme.AyudaCampusTheme
import com.rosalesm.ayudacampus.viewmodel.SesionViewModel
@Composable
fun LoginScreen(vm: SesionViewModel,onLoginExitoso: (Rol)->Unit,onIrARegistro: ()->Unit={}) {
    var correo by rememberSaveable { mutableStateOf("") }; var clave by rememberSaveable { mutableStateOf("") }
    var rol by rememberSaveable { mutableStateOf(Rol.CLIENTE) }
    val error by vm.error.collectAsState(); val cargando by vm.cargando.collectAsState()
    AyudaCampusTheme(darkTheme=false,rol=rol) {
        Surface(Modifier.fillMaxSize(),color=MaterialTheme.colorScheme.surface) {
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).systemBarsPadding().padding(24.dp),verticalArrangement=Arrangement.spacedBy(14.dp)) {
                Spacer(Modifier.height(42.dp))
                Column { Text("Bienvenido",style=MaterialTheme.typography.headlineMedium); Text("Ingresa para continuar",style=MaterialTheme.typography.bodyMedium,color=MaterialTheme.colorScheme.onSurfaceVariant,modifier=Modifier.padding(top=4.dp)) }
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainerHighest,RoundedCornerShape(14.dp)).padding(4.dp),horizontalArrangement=Arrangement.spacedBy(4.dp)) {
                    listOf(Rol.CLIENTE to "Estudiante",Rol.ADMIN to "Soporte TI").forEach { (r,t) ->
                        Button(onClick={ rol=r; vm.limpiarError() },modifier=Modifier.weight(1f).height(38.dp),shape=RoundedCornerShape(11.dp),contentPadding=PaddingValues(0.dp),colors=ButtonDefaults.buttonColors(containerColor=if(rol==r) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,contentColor=if(rol==r) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)) { Text(t,style=MaterialTheme.typography.labelMedium) }
                    }
                }
                Spacer(Modifier.height(6.dp))
                AyudaTextField(correo,{ correo=it; vm.limpiarError() },"Correo",icono=Icons.Outlined.Email)
                AyudaTextField(clave,{ clave=it; vm.limpiarError() },"Contraseña",isPassword=true,icono=Icons.Outlined.Lock)
                MensajeError(error)
                Spacer(Modifier.height(5.dp))
                AyudaButton(if(cargando) "Ingresando…" else "Ingresar",{ vm.login(correo,clave,rol,onLoginExitoso) },enabled=!cargando)
                if(rol==Rol.CLIENTE) TextButton(onClick=onIrARegistro,modifier=Modifier.fillMaxWidth()) { Text("¿No tienes cuenta?  Regístrate",style=MaterialTheme.typography.labelMedium) }
                else Surface(color=MaterialTheme.colorScheme.primaryContainer.copy(alpha=.45f),shape=RoundedCornerShape(12.dp),modifier=Modifier.fillMaxWidth().padding(top=8.dp)) {
                    Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) { Text("Modo práctica",color=MaterialTheme.colorScheme.primary,style=MaterialTheme.typography.titleSmall); Text("Puedes cambiar de perfil desde Ajustes para probar ambos roles.",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            }
        }
    }
}
