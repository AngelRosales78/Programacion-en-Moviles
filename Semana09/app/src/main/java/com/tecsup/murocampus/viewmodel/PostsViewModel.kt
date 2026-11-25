package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.data.mensajeDe
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.Post
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista = _lista.asStateFlow()

    // null = se ve la lista
    private val _idSeleccionado = MutableStateFlow<Int?>(null)
    val idSeleccionado = _idSeleccionado.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle = _detalle.asStateFlow()

    private var trabajoDetalle: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            repo.listar()
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun abrir(id: Int) {
        trabajoDetalle?.cancel()
        _idSeleccionado.value = id
        _detalle.value = UiState.Cargando
        trabajoDetalle = viewModelScope.launch {
            // Las dos llamadas van a la vez; si una falla, falla todo el detalle
            pedirDetalle(id)
                .onSuccess { _detalle.value = UiState.Exito(it) }
                .onFailure { _detalle.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun cerrarDetalle() {
        trabajoDetalle?.cancel()
        _idSeleccionado.value = null
    }

    private suspend fun pedirDetalle(id: Int): Result<DetalleUi> = coroutineScope {
        val pedidoPost = async { repo.detalle(id) }
        val pedidoComentarios = async { repo.comentarios(id) }
        val post = pedidoPost.await()
        val comentarios = pedidoComentarios.await()
        post.mapCatching { DetalleUi(it, comentarios.getOrThrow()) }
    }
}
