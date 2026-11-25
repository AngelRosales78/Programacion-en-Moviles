package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.model.Post
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

// null = Todos
private val opcionesFiltro: List<Int?> = listOf(null) + (1..10)

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    posicion: LazyListState,
    posicionFiltro: LazyListState,
    modifier: Modifier = Modifier
) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()

    Column(modifier.fillMaxSize()) {
        FilaFiltro(
            seleccionado = filtro,
            posicion = posicionFiltro,
            onElegir = { usuario ->
                posicion.requestScrollToItem(0)
                vm.filtrar(usuario)
            }
        )
        when (val e = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorConReintento(e.mensaje, vm::cargar)
            is UiState.Exito ->
                if (e.datos.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Sin publicaciones")
                    }
                } else {
                    ListaPosts(e.datos, posicion, vm::abrir)
                }
        }
    }
}

@Composable
private fun FilaFiltro(
    seleccionado: Int?,
    posicion: LazyListState,
    onElegir: (Int?) -> Unit
) {
    LazyRow(
        state = posicion,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(opcionesFiltro) { usuario ->
            FilterChip(
                selected = usuario == seleccionado,
                onClick = { onElegir(usuario) },
                label = { Text(if (usuario == null) "Todos" else "Usuario $usuario") }
            )
        }
    }
}

@Composable
private fun ListaPosts(
    posts: List<Post>,
    posicion: LazyListState,
    onAbrir: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = posicion,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts, key = { it.id }) { post ->
            TarjetaPost(
                usuario = post.userId,
                titulo = post.title,
                texto = post.body,
                onClick = { onAbrir(post.id) }
            )
        }
    }
}
