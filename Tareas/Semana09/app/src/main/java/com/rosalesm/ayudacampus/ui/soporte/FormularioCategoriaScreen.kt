package com.rosalesm.ayudacampus.ui.soporte
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.cliente.AjusteSwitch
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.CategoriasViewModel
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormularioCategoriaScreen(vm: CategoriasViewModel,id: Int,onVolver: () -> Unit) {
    val categorias by vm.categorias.collectAsState(); val error by vm.error.collectAsState(); val ocupado by vm.ocupado.collectAsState()
    val original=categorias.find { it.id==id }
    if(id!=0 && original==null) { Text("Cargando categoría…"); return }
    var nombre by rememberSaveable(id) { mutableStateOf(original?.nombre ?: "") }
    var icono by rememberSaveable(id) { mutableStateOf(original?.icono ?: "tag") }
    var activa by rememberSaveable(id) { mutableStateOf(original?.activa ?: true) }
    var intento by rememberSaveable(id) { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),verticalArrangement=Arrangement.spacedBy(14.dp)) {
        Cabecera(if(id==0) "Nueva categoría" else "Editar categoría",onVolver=onVolver)
        AyudaTextField(nombre,{ nombre=it },"Nombre",isError=intento && nombre.isBlank(),errorMessage="El nombre es obligatorio")
        Text("Icono")
        FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) { listOf("pc","code","wifi","key","printer","tag").forEach { i ->
            FilterChip(selected=icono==i,onClick={ icono=i },label={ Text(i) },leadingIcon={ Icon(iconoCategoria(i),null,Modifier.size(20.dp)) })
        } }
        AjusteSwitch("Activa",activa,{ activa=it })
        Card { Row(Modifier.padding(20.dp),horizontalArrangement=Arrangement.spacedBy(12.dp)) { Icon(iconoCategoria(icono),null); Text(nombre.ifBlank { "Vista previa" }) } }
        MensajeError(error)
        AyudaButton("Guardar categoría",{ intento=true; vm.guardarCategoria(nombre,icono,activa,id,onVolver) },enabled=!ocupado)
    }
}
