package com.rosalesm.ayudacampus.ui.soporte
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.BandejaViewModel
import com.rosalesm.ayudacampus.model.Prioridad
@Composable
fun BandejaScreen(vm: BandejaViewModel,onVolver: ()->Unit={},onTicketClick: (Int)->Unit) {
    val sla by vm.sla.collectAsState(); val texto by vm.busqueda.collectAsState(); val prioridad by vm.filtroPrioridad.collectAsState(); val categoria by vm.filtroCategoria.collectAsState(); val categorias by vm.categorias.collectAsState(); val tickets by vm.tickets.collectAsState(); val estado by vm.filtroEstado.collectAsState()
    var avanzado by rememberSaveable { mutableStateOf(false) }
    LazyColumn(Modifier.fillMaxSize().padding(horizontal=18.dp),verticalArrangement=Arrangement.spacedBy(8.dp),contentPadding=PaddingValues(bottom=20.dp)) {
        item { Cabecera("Tickets",onVolver=onVolver) }
        item { Row(horizontalArrangement=Arrangement.spacedBy(7.dp)) {
            AyudaTextField(texto,{ vm.busqueda.value=it },"Buscar título, número o estudiante",Modifier.weight(1f),icono=Icons.Outlined.Search)
            IconButton(onClick={ avanzado=!avanzado },modifier=Modifier.padding(top=8.dp)) { Icon(Icons.Outlined.Tune,"Filtros avanzados",tint=MaterialTheme.colorScheme.primary) }
        } }
        if(avanzado) {
            item { Text("Prioridad",style=MaterialTheme.typography.labelMedium); Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                Pill("Todas",prioridad==null,{ vm.filtroPrioridad.value=null }); Prioridad.entries.forEach { p->Pill(etiqueta(p.name),prioridad==p,{ vm.filtroPrioridad.value=p }) }
            } }
            item { Text("Categoría",style=MaterialTheme.typography.labelMedium); Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                Pill("Todas",categoria==null,{ vm.filtroCategoria.value=null }); categorias.forEach { c->Pill(c.nombre,categoria==c.id,{ vm.filtroCategoria.value=c.id }) }
            } }
            item { TextButton(onClick=vm::limpiarFiltros) { Text("Limpiar filtros") } }
        }
        item { Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) { listOf("Todos","Abiertos","En proceso","Resueltos","Cerrados").forEach { f->Pill(f,estado==f,{ vm.filtroEstado.value=f }) } } }
        if(tickets.isEmpty()) item { Text("No hay tickets con estos filtros",Modifier.padding(20.dp)) }
        items(tickets,key={ it.ticket.id }) { t->TicketCard(t,sla=sla[t.ticket.id],mostrarCliente=true,onClick={ onTicketClick(t.ticket.id) }) }
    }
}
