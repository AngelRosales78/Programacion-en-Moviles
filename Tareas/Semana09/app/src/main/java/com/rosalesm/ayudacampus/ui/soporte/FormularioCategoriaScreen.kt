package com.rosalesm.ayudacampus.ui.soporte
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.CategoriasViewModel
@Composable
fun FormularioCategoriaScreen(vm: CategoriasViewModel,id: Int,onVolver: ()->Unit) {
    val categorias by vm.categorias.collectAsState(); val conteos by vm.conteos.collectAsState(); val error by vm.error.collectAsState(); val ocupado by vm.ocupado.collectAsState()
    val original=categorias.find { it.id==id }
    if(id!=0 && original==null) { Text("Cargando categoría…"); return }
    var nombre by rememberSaveable(id) { mutableStateOf(original?.nombre ?: "") }; var icono by rememberSaveable(id) { mutableStateOf(original?.icono ?: "tag") }; var activa by rememberSaveable(id) { mutableStateOf(original?.activa ?: true) }; var intento by rememberSaveable(id) { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().systemBarsPadding().verticalScroll(rememberScrollState()).padding(horizontal=18.dp),verticalArrangement=Arrangement.spacedBy(15.dp)) {
        Cabecera(if(id==0) "Nueva categoría" else "Editar categoría",onVolver=onVolver)
        AyudaTextField(nombre,{ nombre=it },"Nombre de la categoría",isError=intento && nombre.isBlank(),errorMessage="El nombre es obligatorio")
        Text("Ícono",style=MaterialTheme.typography.titleMedium)
        listOf("pc","code","wifi","key","printer","tag").chunked(3).forEach { fila->Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) { fila.forEach { i->
            Surface(Modifier.weight(1f).height(64.dp).semantics { contentDescription="Icono $i" }.clickable { icono=i },shape=RoundedCornerShape(13.dp),color=if(icono==i) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,border=BorderStroke(1.dp,if(icono==i) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)) { Box(contentAlignment=Alignment.Center) { Icon(iconoCategoria(i),null,Modifier.size(26.dp),tint=if(icono==i) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant) } }
        } } }
        Tarjeta { Preferencia("Categoría activa","Los estudiantes podrán elegirla",activa,{ activa=it }) }
        Text("Vista previa",style=MaterialTheme.typography.titleMedium)
        Tarjeta { Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(10.dp)) { IconoCaja(iconoCategoria(icono)); Column { Text(nombre.ifBlank { "Nombre de la categoría" },style=MaterialTheme.typography.titleSmall); Text("${conteos.find { it.categoriaId==id }?.cantidad ?: 0} tickets",style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant) } } }
        MensajeError(error)
        Spacer(Modifier.height(6.dp))
        AyudaButton("Guardar categoría",{ intento=true; vm.guardarCategoria(nombre,icono,activa,id,onVolver) },enabled=!ocupado)
        OutlinedButton(onClick=onVolver,modifier=Modifier.fillMaxWidth().height(46.dp),shape=RoundedCornerShape(11.dp)) { Text("Cancelar") }
        if(original!=null) TextButton(onClick={ vm.eliminar(original,onVolver) },enabled=!ocupado,modifier=Modifier.fillMaxWidth(),colors=ButtonDefaults.textButtonColors(contentColor=MaterialTheme.colorScheme.error)) { Text("Eliminar categoría") }
        Spacer(Modifier.height(12.dp))
    }
}
