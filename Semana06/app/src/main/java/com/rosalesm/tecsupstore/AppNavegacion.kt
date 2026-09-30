package com.rosalesm.tecsupstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var destinoActual by remember { mutableStateOf("Inicio") }
    val favoritosIds = remember { mutableStateListOf<Int>() }

    val categorias = listOf("Más vendidos", "Laptops", "Celulares", "Accesorios")
    val productos = remember {
        listOf(
            Producto(1, "Audífonos", 89.0, "Más vendidos"),
            Producto(2, "Smartwatch", 199.0, "Más vendidos"),
            Producto(3, "Funda celular", 25.0, "Más vendidos")
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                destinoActual = destinoActual,
                cantidadFavoritos = favoritosIds.size,
                onNavegar = { nuevoDestino ->
                    destinoActual = nuevoDestino
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("TECSUP Store") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (destinoActual) {
                    "Inicio" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                items(categorias) { cat ->
                                    FilterChip(
                                        selected = cat == "Más vendidos",
                                        onClick = { },
                                        label = { Text(cat) }
                                    )
                                }
                            }

                            Text(
                                text = "Más vendidos",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            LazyColumn {
                                items(productos, key = { it.id }) { prod ->
                                    val isFav = favoritosIds.contains(prod.id)
                                    TarjetaProducto(
                                        producto = prod,
                                        isFavorito = isFav,
                                        onToggleFavorito = {
                                            if (isFav) {
                                                favoritosIds.remove(prod.id)
                                            } else {
                                                favoritosIds.add(prod.id)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                    "Favoritos" -> {
                        val productosFav = productos.filter { favoritosIds.contains(it.id) }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Mis Favoritos",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (productosFav.isEmpty()) {
                                Text("No tienes productos agregados a favoritos.")
                            } else {
                                LazyColumn {
                                    items(productosFav, key = { it.id }) { prod ->
                                        TarjetaProducto(
                                            producto = prod,
                                            isFavorito = true,
                                            onToggleFavorito = { favoritosIds.remove(prod.id) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Pantalla: $destinoActual",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            }
        }
    }
}