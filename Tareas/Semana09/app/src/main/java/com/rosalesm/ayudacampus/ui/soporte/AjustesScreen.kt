package com.rosalesm.ayudacampus.ui.soporte
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.cliente.AjusteSwitch
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.SesionViewModel
@Composable
fun AjustesScreen(vm: SesionViewModel) {
    val usuario by vm.sesion.collectAsState(); val oscuro by vm.esModoOscuro.collectAsState()
    val notificaciones by vm.notificaciones.collectAsState(); val recibir by vm.recibirTickets.collectAsState(); val error by vm.error.collectAsState()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
        Cabecera("Ajustes","Soporte TI · AyudaCampus")
        Text(usuario?.nombre.orEmpty(),style=MaterialTheme.typography.titleLarge); Text(usuario?.correo.orEmpty())
        AjusteSwitch("Modo oscuro",oscuro,vm::cambiarModoOscuro)
        AjusteSwitch("Notificaciones",notificaciones,vm::cambiarNotificaciones)
        AjusteSwitch("Recibir tickets",recibir,vm::cambiarRecibirTickets)
        Text("Al desactivar la recepción, los estudiantes verán Mesa de ayuda cerrada.")
        OutlinedButton(onClick=vm::cambiarPerfil) { Text("Cambiar a Estudiante") }
        OutlinedButton(onClick={ vm.cerrarSesion() }) { Text("Cerrar sesión") }
        MensajeError(error)
    }
}
