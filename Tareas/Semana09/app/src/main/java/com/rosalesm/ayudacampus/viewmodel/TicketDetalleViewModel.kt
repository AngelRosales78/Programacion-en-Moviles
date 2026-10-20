package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.local.entity.Usuario
import com.rosalesm.ayudacampus.data.local.relation.TicketConDetalle
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TicketDetalleViewModel(
    private val repository: AyudaRepository,
    val ticketId: Int,
    val usuarioActual: Usuario
) : ViewModel() {

    val ticket: StateFlow<TicketConDetalle?> = repository.observarTicket(ticketId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun agregarComentario(texto: String) {
        if (texto.isBlank()) return
        viewModelScope.launch {
            repository.agregarComentario(ticketId, usuarioActual, texto.trim())
        }
    }

    fun cerrarConCalificacion(calificacion: Int) {
        if (calificacion <= 0) return
        viewModelScope.launch {
            repository.cerrarConCalificacion(ticketId, calificacion)
        }
    }

    fun reabrir() {
        viewModelScope.launch {
            repository.reabrir(ticketId)
        }
    }
}