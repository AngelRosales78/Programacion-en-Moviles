package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.local.entity.Categoria
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import com.rosalesm.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NuevoTicketViewModel(
    private val repository: AyudaRepository,
    val clienteId: Int
) : ViewModel() {

    val categoriasActivas: StateFlow<List<Categoria>> = repository.observarCategoriasActivas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _categoriaSeleccionada = MutableStateFlow<Categoria?>(null)
    val categoriaSeleccionada: StateFlow<Categoria?> = _categoriaSeleccionada

    private val _titulo = MutableStateFlow("")
    val titulo: StateFlow<String> = _titulo

    private val _ubicacion = MutableStateFlow("")
    val ubicacion: StateFlow<String> = _ubicacion

    private val _prioridad = MutableStateFlow(Prioridad.MEDIA)
    val prioridad: StateFlow<Prioridad> = _prioridad

    private val _descripcion = MutableStateFlow("")
    val descripcion: StateFlow<String> = _descripcion

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun onCategoriaChange(cat: Categoria) { _categoriaSeleccionada.value = cat }
    fun onTituloChange(nuevoTitulo: String) { _titulo.value = nuevoTitulo }
    fun onUbicacionChange(nuevaUbicacion: String) { _ubicacion.value = nuevaUbicacion }
    fun onPrioridadChange(nuevaPrioridad: Prioridad) { _prioridad.value = nuevaPrioridad }
    fun onDescripcionChange(nuevaDesc: String) { _descripcion.value = nuevaDesc }

    fun enviarTicket(onExito: () -> Unit) {
        val cat = _categoriaSeleccionada.value
        val tit = _titulo.value.trim()
        val desc = _descripcion.value.trim()

        if (cat == null) {
            _error.value = "Selecciona una categoría"
            return
        }
        if (tit.length < 5) {
            _error.value = "El título debe tener al menos 5 caracteres"
            return
        }
        if (desc.length < 10) {
            _error.value = "La descripción debe tener al menos 10 caracteres"
            return
        }

        viewModelScope.launch {
            repository.crearTicket(
                clienteId = clienteId,
                categoriaId = cat.id,
                titulo = tit,
                descripcion = desc,
                ubicacion = _ubicacion.value.ifBlank { "Sin ubicación" },
                prioridad = _prioridad.value
            )
            _error.value = null
            onExito()
        }
    }
}