package com.rosalesm.ayudacampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.model.Prioridad
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.NuevoTicketViewModel
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NuevoTicketScreen(vm: NuevoTicketViewModel,onVolver: ()->Unit,onTicketEnviado: ()->Unit) {
    val categorias by vm.categoriasActivas.collectAsState(); val categoria by vm.categoriaSeleccionada.collectAsState()
    val titulo by vm.titulo.collectAsState(); val descripcion by vm.descripcion.collectAsState(); val ubicacion by vm.ubicacion.collectAsState()
    val prioridad by vm.prioridad.collectAsState(); val errores by vm.errores.collectAsState(); val error by vm.error.collectAsState()
    val recibiendo by vm.recibirTickets.collectAsState(); val enviando by vm.enviando.collectAsState()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Cabecera("Nuevo ticket",onVolver=onVolver)
        if(!recibiendo) Tarjeta { Text("Mesa de ayuda cerrada",color=MaterialTheme.colorScheme.error) }
        Text("Categoría",style=MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement=Arrangement.spacedBy(6.dp)) { categorias.forEach { c -> Pill(c.nombre,c.id==categoria?.id,{ vm.categoriaSeleccionada.value=c },icono=iconoCategoria(c.icono)) } }
        MensajeError(errores["categoria"])
        AyudaTextField(titulo,{ vm.titulo.value=it },"Título",isError=errores.containsKey("titulo"),errorMessage=errores["titulo"])
        AyudaTextField(ubicacion,{ vm.ubicacion.value=it },"Ubicación",icono=Icons.Outlined.LocationOn)
        Text("Prioridad",style=MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement=Arrangement.spacedBy(7.dp)) { Prioridad.entries.forEach { p -> Pill(etiqueta(p.name),p==prioridad,{ vm.prioridad.value=p },Modifier.weight(1f),colorPrioridad(p.name)) } }
        AyudaTextField(descripcion,{ vm.descripcion.value=it },"Descripción",singleLine=false,isError=errores.containsKey("descripcion"),errorMessage=errores["descripcion"])
        Surface(color=MaterialTheme.colorScheme.primaryContainer.copy(alpha=.3f),shape=androidx.compose.foundation.shape.RoundedCornerShape(10.dp)) { Row(Modifier.fillMaxWidth().padding(13.dp),horizontalArrangement=Arrangement.spacedBy(9.dp)) {
            Icon(Icons.Outlined.Schedule,null,Modifier.size(17.dp),tint=MaterialTheme.colorScheme.primary)
            Text("Tiempo de respuesta estimado: ${when(prioridad) { Prioridad.ALTA->4; Prioridad.MEDIA->24; Prioridad.BAJA->72 }} horas",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
        } }
        MensajeError(error)
        AyudaButton(if(enviando) "Enviando…" else "Enviar ticket",{ vm.enviarTicket(onTicketEnviado) },enabled=recibiendo && !enviando)
        Spacer(Modifier.height(12.dp))
    }
}
