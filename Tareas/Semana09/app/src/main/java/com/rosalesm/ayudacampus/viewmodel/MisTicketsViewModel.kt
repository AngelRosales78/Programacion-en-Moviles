package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.local.relation.TicketConDetalle
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.*

class MisTicketsViewModel(
    private val repository: AyudaRepository,
    val clienteId: Int
) : ViewModel() {

    val todos = repository.observarTicketsCliente(clienteId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda

    private val _filtroSeleccionado = MutableStateFlow("Todos")
    val filtroSeleccionado: StateFlow<String> = _filtroSeleccionado

    val tickets: StateFlow<List<TicketConDetalle>> = combine(
        repository.observarTicketsCliente(clienteId),
        _filtroSeleccionado,
        _busqueda
    ) { lista, estado, busqueda ->
        lista.filter { t ->
            val coincideEstado = when (estado) {
                "Abiertos" -> t.ticket.estado == "ABIERTO"
                "En proceso" -> t.ticket.estado == "EN_PROCESO"
                "Resueltos" -> t.ticket.estado == "RESUELTO"
                else -> true
            }
            val coincideTexto = t.ticket.titulo.contains(busqueda, ignoreCase = true) ||
                    "#${t.ticket.id}".contains(busqueda)
            coincideEstado && coincideTexto
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onBusquedaChange(nuevoTexto: String) { _busqueda.value = nuevoTexto }
    fun onFiltroChange(nuevoFiltro: String) { _filtroSeleccionado.value = nuevoFiltro }
}