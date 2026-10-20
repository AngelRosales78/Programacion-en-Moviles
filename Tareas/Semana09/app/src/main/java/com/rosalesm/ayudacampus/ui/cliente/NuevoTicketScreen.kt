package com.rosalesm.ayudacampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.model.Prioridad
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.NuevoTicketViewModel
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NuevoTicketScreen(vm: NuevoTicketViewModel,onVolver: () -> Unit,onTicketEnviado: () -> Unit) {
    val categorias by vm.categoriasActivas.collectAsState()
    val categoria by vm.categoriaSeleccionada.collectAsState()
    val titulo by vm.titulo.collectAsState()
    val descripcion by vm.descripcion.collectAsState()
    val ubicacion by vm.ubicacion.collectAsState()
    val prioridad by vm.prioridad.collectAsState()
    val errores by vm.errores.collectAsState()
    val error by vm.error.collectAsState()
    val recibiendo by vm.recibirTickets.collectAsState()
    val enviando by vm.enviando.collectAsState()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Cabecera("Nuevo ticket","Cuéntanos qué ocurrió",onVolver)
        if(!recibiendo) Card { Text("Mesa de ayuda cerrada",Modifier.padding(16.dp)) }
        Text("Categoría",style=MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            categorias.forEach { c -> FilterChip(selected=c.id==categoria?.id,onClick={ vm.categoriaSeleccionada.value=c },label={ Text(c.nombre) },leadingIcon={ Icon(iconoCategoria(c.icono),null,Modifier.size(18.dp)) }) }
        }
        MensajeError(errores["categoria"])
        AyudaTextField(titulo,{ vm.titulo.value=it },"Título",isError=errores.containsKey("titulo"),errorMessage=errores["titulo"])
        AyudaTextField(ubicacion,{ vm.ubicacion.value=it },"Ubicación")
        Text("Prioridad",style=MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { Prioridad.entries.forEach { p -> FilterChip(selected=p==prioridad,onClick={ vm.prioridad.value=p },label={ Text(etiqueta(p.name)) }) } }
        AyudaTextField(descripcion,{ vm.descripcion.value=it },"Descripción",singleLine=false,isError=errores.containsKey("descripcion"),errorMessage=errores["descripcion"])
        MensajeError(error)
        AyudaButton(if(enviando) "Enviando…" else "Enviar ticket",{ vm.enviarTicket(onTicketEnviado) },enabled=recibiendo && !enviando)
    }
}
