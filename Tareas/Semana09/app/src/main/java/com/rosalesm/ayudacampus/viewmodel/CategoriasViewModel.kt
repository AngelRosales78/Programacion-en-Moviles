package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.local.entity.Categoria
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CategoriasViewModel(private val repo: AyudaRepository) : ViewModel() {
    val categorias=repo.observarCategorias().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    val conteos=repo.observarCantidadPorCategoria().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    val error=MutableStateFlow<String?>(null)
    val ocupado=MutableStateFlow(false)
    private fun accion(block: suspend () -> Unit) {
        if(ocupado.value) return
        viewModelScope.launch {
            ocupado.value=true
            try { error.value=null; block() }
            catch(e: Exception) { error.value=e.message ?: "No se pudo modificar la categoría" }
            finally { ocupado.value=false }
        }
    }
    fun alternarActiva(c: Categoria) = accion { repo.cambiarActiva(c.id,!c.activa) }
    fun guardarCategoria(nombre: String,icono: String,activa: Boolean,idExistente: Int=0,onExito: () -> Unit = {}) = accion {
        require(nombre.isNotBlank()) { "El nombre es obligatorio" }
        require(icono in listOf("pc","code","wifi","key","printer","tag")) { "Selecciona un icono válido" }
        repo.guardarCategoria(Categoria(idExistente,nombre.trim(),icono,activa)); onExito()
    }
    fun eliminar(c: Categoria,onExito: () -> Unit = {}) = accion {
        check(!repo.categoriaTieneTickets(c.id)) { "No se puede eliminar una categoría que ya tiene tickets" }
        repo.eliminarCategoria(c); onExito()
    }
}
