package com.rosalesm.ayudacampus.ui.soporte
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.SesionViewModel
@Composable
fun AjustesScreen(vm: SesionViewModel,onVolver: ()->Unit={}) {
    val usuario by vm.sesion.collectAsState(); val oscuro by vm.esModoOscuro.collectAsState()
    val notificaciones by vm.notificaciones.collectAsState(); val recibir by vm.recibirTickets.collectAsState(); val error by vm.error.collectAsState()
    val u=usuario ?: return
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=18.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
        Cabecera("Ajustes",onVolver=onVolver)
        Identidad(u,soporte=true)
        CambiarPerfil("Estudiante",vm::cambiarPerfil)
        Text("Preferencias",style=MaterialTheme.typography.titleMedium)
        Tarjeta {
            Preferencia("Modo oscuro","Cambia el tema de la app",oscuro,vm::cambiarModoOscuro)
            HorizontalDivider(color=MaterialTheme.colorScheme.outlineVariant)
            Preferencia("Notificaciones","Avisar tickets nuevos",notificaciones,vm::cambiarNotificaciones)
            HorizontalDivider(color=MaterialTheme.colorScheme.outlineVariant)
            Preferencia("Recibir tickets","Abrir o cerrar la mesa de ayuda",recibir,vm::cambiarRecibirTickets)
        }
        AvisoPreferencias()
        Spacer(Modifier.height(8.dp))
        BotonSalir { vm.cerrarSesion() }
        MensajeError(error)
    }
}
