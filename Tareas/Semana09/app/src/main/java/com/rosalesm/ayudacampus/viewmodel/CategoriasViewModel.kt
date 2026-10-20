package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.local.entity.Categoria
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriasViewModel(
    private val repo: AyudaRepository
) : ViewModel() {

    val categorias: StateFlow<List<Categoria>> = repo.observarCategorias().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun alternarActiva(categoria: Categoria) {
        viewModelScope.launch {
            repo.cambiarActiva(categoria.id, !categoria.activa)
        }
    }

    fun guardarCategoria(nombre: String, icono: String, activa: Boolean, idExistente: Int = 0) {
        viewModelScope.launch {
            val categoria = Categoria(
                id = idExistente,
                nombre = nombre,
                icono = icono,
                activa = activa
            )
            repo.guardarCategoria(categoria)
        }
    }
}