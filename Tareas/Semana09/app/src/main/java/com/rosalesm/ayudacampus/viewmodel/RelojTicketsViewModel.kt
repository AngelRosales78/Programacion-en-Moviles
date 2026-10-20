package com.rosalesm.ayudacampus.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.local.relation.TicketConDetalle
import com.rosalesm.ayudacampus.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

// One clock per visible screen; the View never calculates deadlines or reads the system time.
open class RelojTicketsViewModel : ViewModel() {
    private val ahora=flow {
        while(true) { emit(System.currentTimeMillis()); delay(60_000L) }
    }
    fun observarSla(tickets: Flow<List<TicketConDetalle>>): StateFlow<Map<Int,EstadoSla>> = combine(tickets,ahora) { lista,reloj ->
        lista.associate { it.ticket.id to calcularSla(it.ticket,reloj) }
    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyMap())
}
