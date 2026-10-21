package com.rosalesm.ayudacampus.ui.cliente
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.viewmodel.MisTicketsViewModel
@Composable
fun MisTicketsScreen(vm: MisTicketsViewModel,nombreUsuario: String,onNuevoTicketClick: ()->Unit,onTicketClick: (Int)->Unit) {
    val sla by vm.sla.collectAsState(); val tickets by vm.tickets.collectAsState(); val todos by vm.todos.collectAsState()
    val texto by vm.busqueda.collectAsState(); val filtro by vm.filtroSeleccionado.collectAsState()
    Scaffold(contentWindowInsets=WindowInsets(0,0,0,0),floatingActionButton={ FloatingActionButton(onClick=onNuevoTicketClick,containerColor=MaterialTheme.colorScheme.primary,contentColor=MaterialTheme.colorScheme.onPrimary) { Icon(Icons.Default.Add,"Crear ticket") } }) { p ->
        LazyColumn(Modifier.fillMaxSize().padding(p).padding(horizontal=18.dp),verticalArrangement=Arrangement.spacedBy(8.dp),contentPadding=PaddingValues(bottom=80.dp)) {
            item { Cabecera("Mis tickets","Hola, ${nombreUsuario.substringBefore(' ')} · ${todos.count { it.ticket.estado=="ABIERTO" }} abiertos") }
            item { AyudaTextField(texto,vm::onBusquedaChange,"Buscar por título o número…",icono=Icons.Outlined.Search) }
            item { Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) { listOf("Todos","Abiertos","En proceso","Resueltos").forEach { f -> Pill(f,f==filtro,{ vm.onFiltroChange(f) }) } } }
            if(tickets.isEmpty()) item { Text("No tienes tickets aquí",Modifier.padding(24.dp),style=MaterialTheme.typography.bodyMedium) }
            items(tickets,key={ it.ticket.id }) { t -> TicketCard(t,sla=sla[t.ticket.id],onClick={ onTicketClick(t.ticket.id) }) }
        }
    }
}
