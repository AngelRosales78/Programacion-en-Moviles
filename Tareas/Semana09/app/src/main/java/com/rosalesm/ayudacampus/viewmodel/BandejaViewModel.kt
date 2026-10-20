package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.local.relation.TicketConDetalle
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.*

class BandejaViewModel(
    private val repository: AyudaRepository
) : ViewModel() {

    val filtroEstado = MutableStateFlow("Todos")

    val tickets: StateFlow<List<TicketConDetalle>> = combine(
        repository.observarTodosLosTickets(),
        filtroEstado
    ) { lista, estado ->
        lista.filter { t ->
            when (estado) {
                "Abiertos" -> t.ticket.estado == "ABIERTO"
                "En proceso" -> t.ticket.estado == "EN_PROCESO"
                "Resueltos" -> t.ticket.estado == "RESUELTO"
                else -> true
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}