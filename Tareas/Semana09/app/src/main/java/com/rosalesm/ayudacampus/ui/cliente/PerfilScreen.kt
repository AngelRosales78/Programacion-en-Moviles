package com.rosalesm.ayudacampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.*
@Composable
fun PerfilScreen(vm: SesionViewModel,ticketsVm: MisTicketsViewModel) {
    val usuario by vm.sesion.collectAsState()
    val tickets by ticketsVm.todos.collectAsState()
    val oscuro by vm.esModoOscuro.collectAsState()
    val notificaciones by vm.notificaciones.collectAsState()
    val error by vm.error.collectAsState()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
        Cabecera("Mi perfil","Estudiante · AyudaCampus")
        Text(usuario?.nombre.orEmpty(),style=MaterialTheme.typography.titleLarge)
        Text(usuario?.correo.orEmpty())
        Card { Column(Modifier.padding(16.dp)) {
            Text("${tickets.size} tickets en total")
            Text("${tickets.count { it.ticket.estado=="ABIERTO" }} abiertos")
            Text("${tickets.count { it.ticket.estado=="RESUELTO" }} resueltos")
        } }
        AjusteSwitch("Modo oscuro",oscuro,vm::cambiarModoOscuro)
        AjusteSwitch("Notificaciones",notificaciones,vm::cambiarNotificaciones)
        OutlinedButton(onClick=vm::cambiarPerfil) { Text("Cambiar a Soporte TI") }
        OutlinedButton(onClick={ vm.cerrarSesion() }) { Text("Cerrar sesión") }
        MensajeError(error)
    }
}
@Composable
fun AjusteSwitch(texto: String,valor: Boolean,onCambio: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) { Text(texto,Modifier.weight(1f)); Switch(checked=valor,onCheckedChange=onCambio) }
}
