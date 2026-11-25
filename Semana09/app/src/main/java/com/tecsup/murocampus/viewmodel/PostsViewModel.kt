package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.data.mensajeDe
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
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

    // null = Todos
    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    // null = se ve la lista
    private val _idSeleccionado = MutableStateFlow<Int?>(null)
    val idSeleccionado = _idSeleccionado.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle = _detalle.asStateFlow()

    // null = aún no se envió
    private val _envio = MutableStateFlow<UiState<Post>?>(null)
    val envio = _envio.asStateFlow()

    private var trabajoLista: Job? = null
    private var trabajoDetalle: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        // Cancela la carga anterior para que no pise el resultado del filtro actual
        trabajoLista?.cancel()
        _lista.value = UiState.Cargando
        trabajoLista = viewModelScope.launch {
            val usuario = _usuarioFiltro.value
            val resultado = if (usuario == null) repo.listar() else repo.porUsuario(usuario)
            resultado
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
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

    fun publicar(titulo: String, texto: String) {
        _envio.value = UiState.Cargando
        viewModelScope.launch {
            repo.crear(NuevoPost(userId = 1, title = titulo, body = texto))
                .onSuccess { _envio.value = UiState.Exito(it) }
                .onFailure { _envio.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun limpiarEnvio() {
        _envio.value = null
    }

    private suspend fun pedirDetalle(id: Int): Result<DetalleUi> = coroutineScope {
        val pedidoPost = async { repo.detalle(id) }
        val pedidoComentarios = async { repo.comentarios(id) }
        val post = pedidoPost.await()
        val comentarios = pedidoComentarios.await()
        post.mapCatching { DetalleUi(it, comentarios.getOrThrow()) }
    }
}
