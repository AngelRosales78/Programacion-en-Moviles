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
@Composable
fun BandejaScreen(vm: BandejaViewModel,onTicketClick: (Int) -> Unit) {
    val tickets by vm.tickets.collectAsState(); val estado by vm.filtroEstado.collectAsState()
    LazyColumn(Modifier.fillMaxSize().padding(horizontal=20.dp),verticalArrangement=Arrangement.spacedBy(10.dp),contentPadding=PaddingValues(bottom=24.dp)) {
        item { Cabecera("Bandeja de tickets","Ordenados por prioridad y fecha") }
        item { Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            listOf("Todos","Abiertos","En proceso","Resueltos").forEach { f -> FilterChip(selected=estado==f,onClick={ vm.filtroEstado.value=f },label={ Text(f) }) }
        } }
        if(tickets.isEmpty()) item { Text("No hay tickets con estos filtros") }
        items(tickets,key={ it.ticket.id }) { t -> TicketCard(t,mostrarCliente=true,onClick={ onTicketClick(t.ticket.id) }) }
    }
}
