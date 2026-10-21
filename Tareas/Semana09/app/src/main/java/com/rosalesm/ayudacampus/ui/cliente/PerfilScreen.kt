package com.rosalesm.ayudacampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.*
@Composable
fun PerfilScreen(vm: SesionViewModel,ticketsVm: MisTicketsViewModel,onVolver: ()->Unit={}) {
    val usuario by vm.sesion.collectAsState(); val tickets by ticketsVm.todos.collectAsState()
    val oscuro by vm.esModoOscuro.collectAsState(); val notificaciones by vm.notificaciones.collectAsState(); val error by vm.error.collectAsState()
    val u=usuario ?: return
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=18.dp),verticalArrangement=Arrangement.spacedBy(14.dp)) {
        Cabecera("Perfil",onVolver=onVolver)
        Identidad(u)
        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            Contador("Abiertos",tickets.count { it.ticket.estado=="ABIERTO" },Color(0xFFD97706),Modifier.weight(1f))
            Contador("Resueltos",tickets.count { it.ticket.estado=="RESUELTO" },Color(0xFF16A34A),Modifier.weight(1f))
            Contador("Total",tickets.size,MaterialTheme.colorScheme.primary,Modifier.weight(1f),"${tickets.size} tickets en total")
        }
        CambiarPerfil("Soporte TI",vm::cambiarPerfil)
        Text("Preferencias",style=MaterialTheme.typography.titleMedium)
        Tarjeta {
            Preferencia("Modo oscuro","Cambia el tema de la app",oscuro,vm::cambiarModoOscuro)
            HorizontalDivider(color=MaterialTheme.colorScheme.outlineVariant)
            Preferencia("Notificaciones","Avisar cambios de estado",notificaciones,vm::cambiarNotificaciones)
        }
        AvisoPreferencias()
        BotonSalir { vm.cerrarSesion() }
        MensajeError(error)
        Spacer(Modifier.height(8.dp))
    }
}
@Composable
fun AjusteSwitch(texto: String,valor: Boolean,onCambio: (Boolean)->Unit) { Preferencia(texto,"",valor,onCambio) }
