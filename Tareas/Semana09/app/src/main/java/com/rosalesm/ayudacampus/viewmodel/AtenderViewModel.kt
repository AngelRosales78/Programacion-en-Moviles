package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.local.entity.Usuario
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import com.rosalesm.ayudacampus.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AtenderViewModel(private val repo: AyudaRepository, private val ticketId: Int, val usuario: Usuario) : ViewModel() {
    val ticket=repo.observarTicket(ticketId).stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),null)
    val estado=MutableStateFlow<EstadoTicket?>(null)
    val prioridad=MutableStateFlow<Prioridad?>(null)
    val error=MutableStateFlow<String?>(null)
    val ocupado=MutableStateFlow(false)
    fun cambiarEstado(e: EstadoTicket) { if(e != EstadoTicket.CERRADO && ticket.value?.ticket?.estado != "CERRADO") estado.value=e }
    fun cambiarPrioridad(p: Prioridad) { if(ticket.value?.ticket?.estado != "CERRADO") prioridad.value=p }
    private fun accion(block: suspend () -> Unit) {
        if(ocupado.value) return
        viewModelScope.launch {
            ocupado.value=true
            try {
                require(usuario.rol == "ADMIN") { "Solo Soporte puede atender" }
                check(repo.observarTicket(ticketId).first()?.ticket?.estado != "CERRADO") { "El ticket está cerrado" }
                error.value=null; block()
            } catch(e: Exception) { error.value=e.message ?: "No se pudo guardar" }
            finally { ocupado.value=false }
        }
    }
    fun responder(texto: String, onExito: () -> Unit = {}) {
        if(texto.isBlank()) return
        accion { repo.agregarComentario(ticketId,usuario,texto.trim()); onExito() }
    }
    fun guardar(onExito: () -> Unit = {}) = accion {
        estado.value?.let { require(it != EstadoTicket.CERRADO); repo.cambiarEstado(ticketId,it) }
        prioridad.value?.let { repo.cambiarPrioridad(ticketId,it) }
        estado.value=null; prioridad.value=null; onExito()
    }
}
