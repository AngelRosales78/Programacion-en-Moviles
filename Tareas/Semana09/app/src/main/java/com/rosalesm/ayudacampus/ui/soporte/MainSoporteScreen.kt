package com.rosalesm.ayudacampus.ui.soporte
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rosalesm.ayudacampus.data.local.entity.Usuario
import com.rosalesm.ayudacampus.ui.components.BarraInferior
import com.rosalesm.ayudacampus.viewmodel.*
@Composable
fun MainSoporteScreen(usuario: Usuario,sesionVm: SesionViewModel,factory: AyudaViewModelFactory) {
    var tab by rememberSaveable { mutableIntStateOf(0) }; var detalleId by rememberSaveable { mutableStateOf<Int?>(null) }; var categoriaId by rememberSaveable { mutableStateOf<Int?>(null) }
    val panel: PanelViewModel=viewModel(factory=factory); val bandeja: BandejaViewModel=viewModel(factory=factory); val categorias: CategoriasViewModel=viewModel(factory=factory)
    BackHandler(detalleId!=null || categoriaId!=null || tab!=0) { if(detalleId!=null) detalleId=null else if(categoriaId!=null) categoriaId=null else tab=0 }
    val cat=categoriaId
    if(cat!=null) FormularioCategoriaScreen(categorias,cat) { categoriaId=null }
    else Scaffold(bottomBar={ BarraInferior(listOf("Panel","Tickets","Categorías","Ajustes"),listOf(Icons.Outlined.BarChart,Icons.Outlined.Menu,Icons.Outlined.Sell,Icons.Outlined.Settings),if(detalleId!=null) 1 else tab) { tab=it; detalleId=null } }) { p ->
        Box(Modifier.fillMaxSize().padding(p)) {
            val id=detalleId
            if(id!=null) {
                val vm: AtenderViewModel=viewModel(key="atender-${usuario.id}-$id",factory=factory.paraUsuario(usuario,id))
                AtenderTicketScreen(vm) { detalleId=null }
            } else when(tab) {
                0->PanelScreen(panel) { bandeja.verAltasAbiertas(); tab=1 }
                1->BandejaScreen(bandeja,onVolver={ tab=0 }) { detalleId=it }
                2->CategoriasScreen(categorias,onVolver={ tab=0 }) { categoriaId=it }
                else->AjustesScreen(sesionVm) { tab=0 }
            }
        }
    }
}
