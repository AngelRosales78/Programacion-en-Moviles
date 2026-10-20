package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.local.entity.Categoria
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import com.rosalesm.ayudacampus.data.prefs.PreferenciasStore
import com.rosalesm.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NuevoTicketViewModel(private val repository: AyudaRepository, val clienteId: Int, private val prefs: PreferenciasStore) : ViewModel() {
    val categoriasActivas = repository.observarCategoriasActivas().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    val recibirTickets = prefs.recibirTickets.stateIn(viewModelScope,SharingStarted.Eagerly,true)
    val categoriaSeleccionada = MutableStateFlow<Categoria?>(null)
    val titulo = MutableStateFlow("")
    val ubicacion = MutableStateFlow("")
    val descripcion = MutableStateFlow("")
    val prioridad = MutableStateFlow(Prioridad.MEDIA)
    val errores = MutableStateFlow<Map<String,String>>(emptyMap())
    val error = MutableStateFlow<String?>(null)
    val enviando = MutableStateFlow(false)
    fun enviarTicket(onExito: () -> Unit) {
        if (enviando.value) return
        val fallos = mutableMapOf<String,String>()
        if (titulo.value.trim().length < 5) fallos["titulo"] = "El título debe tener al menos 5 caracteres"
        if (descripcion.value.trim().length < 10) fallos["descripcion"] = "La descripción debe tener al menos 10 caracteres"
        if (categoriaSeleccionada.value == null) fallos["categoria"] = "Selecciona una categoría activa"
        errores.value = fallos
        if (fallos.isNotEmpty()) return
        viewModelScope.launch {
            enviando.value = true
            try {
                check(prefs.recibirTickets.first()) { "Mesa de ayuda cerrada" }
                val cat = repository.observarCategoriasActivas().first().find { it.id == categoriaSeleccionada.value?.id }
                requireNotNull(cat) { "La categoría ya no está activa. Elige otra" }
                repository.crearTicket(clienteId,cat.id,titulo.value.trim(),descripcion.value.trim(),ubicacion.value.trim(),prioridad.value)
                titulo.value=""; descripcion.value=""; ubicacion.value=""; prioridad.value=Prioridad.MEDIA; categoriaSeleccionada.value=null
                error.value=null
                onExito()
            } catch(e: Exception) { error.value=e.message ?: "No se pudo enviar el ticket" }
            finally { enviando.value=false }
        }
    }
}
