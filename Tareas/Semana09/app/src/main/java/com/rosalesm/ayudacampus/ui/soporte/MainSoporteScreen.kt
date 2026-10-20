package com.rosalesm.ayudacampus.ui.soporte
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
fun MainSoporteScreen(usuario: Usuario,sesionVm: SesionViewModel,factory: AyudaViewModelFactory) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var detalleId by rememberSaveable { mutableStateOf<Int?>(null) }
    var categoriaId by rememberSaveable { mutableStateOf<Int?>(null) }
    val panel: PanelViewModel=viewModel(factory=factory)
    val bandeja: BandejaViewModel=viewModel(factory=factory)
    val categorias: CategoriasViewModel=viewModel(factory=factory)
    BackHandler(detalleId!=null || categoriaId!=null || tab!=0) { if(detalleId!=null) detalleId=null else if(categoriaId!=null) categoriaId=null else tab=0 }
    if(detalleId!=null) {
        val id=detalleId!!
        val vm: AtenderViewModel=viewModel(key="atender-${usuario.id}-$id",factory=factory.paraUsuario(usuario,id))
        AtenderTicketScreen(vm) { detalleId=null }
    } else if(categoriaId!=null) FormularioCategoriaScreen(categorias,categoriaId!!) { categoriaId=null }
    else Scaffold(bottomBar={ NavigationBar {
        listOf("Panel" to Icons.Outlined.BarChart,"Tickets" to Icons.Outlined.ConfirmationNumber,"Categorías" to Icons.Outlined.Category,"Ajustes" to Icons.Outlined.Settings).forEachIndexed { i,(texto,icono) ->
            NavigationBarItem(selected=tab==i,onClick={ tab=i },icon={ Icon(icono,null) },label={ Text(texto) })
        }
    } }) { p -> Box(Modifier.padding(p)) {
        when(tab) {
            0 -> PanelScreen(panel) { bandeja.verAltasAbiertas(); tab=1 }
            1 -> BandejaScreen(bandeja) { detalleId=it }
            2 -> CategoriasScreen(categorias) { categoriaId=it }
            else -> AjustesScreen(sesionVm)
        }
    } }
}
