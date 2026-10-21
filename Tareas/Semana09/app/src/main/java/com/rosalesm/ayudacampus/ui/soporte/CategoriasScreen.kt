package com.rosalesm.ayudacampus.ui.soporte
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.CategoriasViewModel
@Composable
fun CategoriasScreen(vm: CategoriasViewModel,onVolver: ()->Unit={},onEditar: (Int)->Unit) {
    val categorias by vm.categorias.collectAsState(); val conteos by vm.conteos.collectAsState(); val error by vm.error.collectAsState()
    Scaffold(contentWindowInsets=WindowInsets(0,0,0,0),floatingActionButton={ FloatingActionButton(onClick={ onEditar(0) },containerColor=MaterialTheme.colorScheme.primary,contentColor=MaterialTheme.colorScheme.onPrimary) { Icon(Icons.Default.Add,"Nueva categoría") } }) { p->
        LazyColumn(Modifier.fillMaxSize().padding(p).padding(horizontal=18.dp),verticalArrangement=Arrangement.spacedBy(10.dp),contentPadding=PaddingValues(bottom=90.dp)) {
            item { Cabecera("Categorías",onVolver=onVolver); MensajeError(error) }
            items(categorias,key={ it.id }) { c->Tarjeta {
                Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                    IconoCaja(iconoCategoria(c.icono))
                    Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(4.dp)) { Text(c.nombre,style=MaterialTheme.typography.titleSmall); val n=conteos.find { it.categoriaId==c.id }?.cantidad ?: 0; Text("$n ${if(n==1) "ticket" else "tickets"}",style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant) }
                    Switch(checked=c.activa,onCheckedChange={ vm.alternarActiva(c) },modifier=Modifier.height(34.dp))
                    IconButton(onClick={ onEditar(c.id) },modifier=Modifier.size(30.dp)) { Icon(Icons.Outlined.Edit,"Editar ${c.nombre}",Modifier.size(19.dp),tint=MaterialTheme.colorScheme.primary) }
                }
            } }
        }
    }
}
