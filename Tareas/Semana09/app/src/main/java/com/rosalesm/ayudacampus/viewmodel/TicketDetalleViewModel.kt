package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.local.entity.Usuario
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TicketDetalleViewModel(private val repository: AyudaRepository, val ticketId: Int, val usuarioActual: Usuario) : RelojTicketsViewModel() {
    val ticket = repository.observarTicket(ticketId).map { it?.takeIf { t -> t.ticket.clienteId == usuarioActual.id && usuarioActual.rol == "CLIENTE" } }
        .stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),null)
    val error = MutableStateFlow<String?>(null)
    val ocupado = MutableStateFlow(false)
    private fun accion(block: suspend () -> Unit) {
        if(ocupado.value) return
        viewModelScope.launch {
            ocupado.value=true
            try { error.value=null; block() }
            catch(e: Exception) { error.value=e.message ?: "No se pudo guardar" }
            finally { ocupado.value=false }
        }
    }
    private suspend fun actual() = requireNotNull(repository.observarTicket(ticketId).first()).also {
        require(usuarioActual.rol == "CLIENTE" && it.ticket.clienteId == usuarioActual.id) { "No tienes acceso a este ticket" }
    }
    fun agregarComentario(texto: String, onExito: () -> Unit = {}) {
        if(texto.isBlank()) return
        accion {
            check(actual().ticket.estado != "CERRADO") { "El ticket está cerrado" }
            repository.agregarComentario(ticketId,usuarioActual,texto.trim()); onExito()
        }
    }
    fun cerrarConCalificacion(calificacion: Int, comentario: String = "") = accion {
        check(actual().ticket.estado == "RESUELTO") { "Solo puedes cerrar tickets resueltos" }
        require(calificacion in 1..5) { "Selecciona de 1 a 5 estrellas" }
        if(comentario.isNotBlank()) repository.agregarComentario(ticketId,usuarioActual,comentario.trim())
        repository.cerrarConCalificacion(ticketId,calificacion)
    }
    fun reabrir() = accion {
        check(actual().ticket.estado == "RESUELTO") { "Solo puedes reabrir tickets resueltos" }
        repository.reabrir(ticketId)
    }
    val sla = observarSla(ticket.map { listOfNotNull(it) })
}
