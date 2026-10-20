package com.rosalesm.ayudacampus.ui.soporte
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.BandejaViewModel
import com.rosalesm.ayudacampus.model.Prioridad
@Composable
fun BandejaScreen(vm: BandejaViewModel,onTicketClick: (Int) -> Unit) {
    val sla by vm.sla.collectAsState()
    val texto by vm.busqueda.collectAsState(); val prioridad by vm.filtroPrioridad.collectAsState()
    val categoria by vm.filtroCategoria.collectAsState(); val categorias by vm.categorias.collectAsState()
    val tickets by vm.tickets.collectAsState(); val estado by vm.filtroEstado.collectAsState()
    LazyColumn(Modifier.fillMaxSize().padding(horizontal=20.dp),verticalArrangement=Arrangement.spacedBy(10.dp),contentPadding=PaddingValues(bottom=24.dp)) {
        item { Cabecera("Bandeja de tickets","Ordenados por prioridad y fecha") }
        item { AyudaTextField(texto,{ vm.busqueda.value=it },"Buscar título, número o estudiante") }
        item { Text("Prioridad",style=MaterialTheme.typography.labelLarge)
            Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                FilterChip(selected=prioridad==null,onClick={ vm.filtroPrioridad.value=null },label={ Text("Todas") })
                Prioridad.entries.forEach { p -> FilterChip(selected=prioridad==p,onClick={ vm.filtroPrioridad.value=p },label={ Text(etiqueta(p.name)) }) }
            }
        }
        item { Text("Categoría",style=MaterialTheme.typography.labelLarge)
            Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                FilterChip(selected=categoria==null,onClick={ vm.filtroCategoria.value=null },label={ Text("Todas") })
                categorias.forEach { c -> FilterChip(selected=categoria==c.id,onClick={ vm.filtroCategoria.value=c.id },label={ Text(c.nombre) }) }
            }
        }
        item { TextButton(onClick=vm::limpiarFiltros) { Text("Limpiar filtros") } }
        item { Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            listOf("Todos","Abiertos","En proceso","Resueltos","Cerrados").forEach { f -> FilterChip(selected=estado==f,onClick={ vm.filtroEstado.value=f },label={ Text(f) }) }
        } }
        if(tickets.isEmpty()) item { Text("No hay tickets con estos filtros") }
        items(tickets,key={ it.ticket.id }) { t -> TicketCard(t,sla=sla[t.ticket.id],mostrarCliente=true,onClick={ onTicketClick(t.ticket.id) }) }
    }
}
