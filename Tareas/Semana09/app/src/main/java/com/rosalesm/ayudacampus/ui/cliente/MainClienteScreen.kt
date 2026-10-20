package com.rosalesm.ayudacampus.ui.cliente
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rosalesm.ayudacampus.data.local.entity.Usuario
import com.rosalesm.ayudacampus.viewmodel.*

@Composable
fun MainClienteScreen(usuario: Usuario,sesionVm: SesionViewModel,factory: AyudaViewModelFactory) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var detalleId by rememberSaveable { mutableStateOf<Int?>(null) }
    val f=remember(usuario.id) { factory.paraUsuario(usuario) }
    val mis: MisTicketsViewModel=viewModel(key="mis-${usuario.id}",factory=f)
    val nuevo: NuevoTicketViewModel=viewModel(key="nuevo-${usuario.id}",factory=f)
    BackHandler(detalleId!=null || tab!=0) { if(detalleId!=null) detalleId=null else tab=0 }
    if(detalleId!=null) {
        val id=detalleId!!
        val vm: TicketDetalleViewModel=viewModel(key="detalle-${usuario.id}-$id",factory=factory.paraUsuario(usuario,id))
        DetalleTicketScreen(vm) { detalleId=null }
    } else Scaffold(bottomBar={
        NavigationBar {
            listOf("Tickets" to Icons.Outlined.ConfirmationNumber,"Nuevo" to Icons.Outlined.AddBox,"Perfil" to Icons.Outlined.Person).forEachIndexed { i,(nombre,icono) ->
                NavigationBarItem(selected=tab==i,onClick={ tab=i },icon={ Icon(icono,null) },label={ Text(nombre) })
            }
        }
    }) { padding -> Box(Modifier.padding(padding)) {
        when(tab) {
            0 -> MisTicketsScreen(mis,usuario.nombre,onNuevoTicketClick={ tab=1 },onTicketClick={ detalleId=it })
            1 -> NuevoTicketScreen(nuevo,onVolver={ tab=0 },onTicketEnviado={ tab=0 })
            else -> PerfilScreen(sesionVm,mis)
        }
    } }
}
