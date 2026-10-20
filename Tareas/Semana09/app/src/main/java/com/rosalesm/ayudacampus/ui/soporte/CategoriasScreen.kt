package com.rosalesm.ayudacampus.ui.soporte
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.CategoriasViewModel
@Composable
fun CategoriasScreen(vm: CategoriasViewModel,onEditar: (Int) -> Unit) {
    val categorias by vm.categorias.collectAsState(); val conteos by vm.conteos.collectAsState(); val error by vm.error.collectAsState()
    LazyColumn(Modifier.fillMaxSize().padding(horizontal=20.dp),verticalArrangement=Arrangement.spacedBy(12.dp),contentPadding=PaddingValues(bottom=24.dp)) {
        item { Cabecera("Categorías","Administra las categorías de atención") }
        item { Button(onClick={ onEditar(0) }) { Text("Nueva categoría") }; MensajeError(error) }
        items(categorias,key={ it.id }) { c -> Card {
            Column(Modifier.padding(14.dp)) {
                Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                    Icon(iconoCategoria(c.icono),null); Text(c.nombre,Modifier.weight(1f)); Switch(checked=c.activa,onCheckedChange={ vm.alternarActiva(c) })
                }
                Text("${conteos.find { it.categoriaId==c.id }?.cantidad ?: 0} tickets")
                Row { TextButton(onClick={ onEditar(c.id) }) { Text("Editar") }; TextButton(onClick={ vm.eliminar(c) }) { Text("Eliminar") } }
            }
        } }
    }
}
