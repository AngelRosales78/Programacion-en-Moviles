package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    posicion: LazyListState,
    modifier: Modifier = Modifier
) {
    val estado by vm.lista.collectAsStateWithLifecycle()

    when (val e = estado) {
        UiState.Cargando -> Cargando(modifier)
        is UiState.Error -> ErrorConReintento(e.mensaje, vm::cargar, modifier)
        is UiState.Exito -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = posicion,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(e.datos, key = { it.id }) { post ->
                TarjetaPost(
                    usuario = post.userId,
                    titulo = post.title,
                    texto = post.body,
                    onClick = { vm.abrir(post.id) }
                )
            }
        }
    }
}
