package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.mibodega.ui.cliente.modelo.*
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

private enum class OrdenPrecio { NINGUNO, MENOR_A_MAYOR, MAYOR_A_MENOR }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    productos: List<Producto> = listaProductosFake,
    cantidadCarrito: Int = 0,
    usuario: Usuario? = null,
    pedidos: List<Pedido> = emptyList(),
    modoOscuro: Boolean = false,
    onToggleModoOscuro: (Boolean) -> Unit = {},
    onVerCarrito: () -> Unit = {},
    onProductoClick: (Producto) -> Unit = {},
    onAgregarProducto: (Producto) -> Unit = {},
    onToggleFavorito: (Producto) -> Unit = {}
) {
    var pestanaSeleccionada by remember { mutableStateOf(0) }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    var textoBusqueda by remember { mutableStateOf("") }
    var ordenPrecio by remember { mutableStateOf(OrdenPrecio.NINGUNO) }
    var mostrarSoloFavoritos by remember { mutableStateOf(false) }

    // Filtrado combinado y ordenamiento por precio
    val productosFiltrados = productos.filter { producto ->
        val coincideCategoria = categoriaSeleccionada == "Todos" || producto.categoria == categoriaSeleccionada
        val coincideBusqueda = producto.nombre.contains(textoBusqueda, ignoreCase = true) ||
                producto.descripcion.contains(textoBusqueda, ignoreCase = true)
        val coincideFavorito = !mostrarSoloFavoritos || producto.esFavorito
        coincideCategoria && coincideBusqueda && coincideFavorito
    }.let { lista ->
        when (ordenPrecio) {
            OrdenPrecio.MENOR_A_MAYOR -> lista.sortedBy { it.precio }
            OrdenPrecio.MAYOR_A_MENOR -> lista.sortedByDescending { it.precio }
            OrdenPrecio.NINGUNO -> lista
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Bodega", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onVerCarrito) {
                        BadgedBox(
                            badge = {
                                if (cantidadCarrito > 0) {
                                    Badge { Text("$cantidadCarrito") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
            )
        },
        bottomBar = {
            BarraInferior(
                seleccionado = pestanaSeleccionada,
                onSeleccionar = { pestanaSeleccionada = it }
            )
        }
    ) { paddingInterno ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
        ) {
            when (pestanaSeleccionada) {
                0 -> {
                    // PESTAÑA 0: INICIO Y CATÁLOGO
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        OutlinedTextField(
                            value = textoBusqueda,
                            onValueChange = { textoBusqueda = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            placeholder = { Text("Buscar productos...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = GrisClaro,
                                focusedContainerColor = GrisClaro,
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = VerdeBodega
                            )
                        )

                        // Chips de ordenamiento por precio y filtro de favoritos
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                selected = mostrarSoloFavoritos,
                                onClick = { mostrarSoloFavoritos = !mostrarSoloFavoritos },
                                label = { Text("Favoritos") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (mostrarSoloFavoritos) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = null,
                                        tint = if (mostrarSoloFavoritos) Color.Red else Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                FilterChip(
                                    selected = ordenPrecio == OrdenPrecio.MENOR_A_MAYOR,
                                    onClick = {
                                        ordenPrecio = if (ordenPrecio == OrdenPrecio.MENOR_A_MAYOR) OrdenPrecio.NINGUNO else OrdenPrecio.MENOR_A_MAYOR
                                    },
                                    label = { Text("Precio:  ↓") }
                                )
                                FilterChip(
                                    selected = ordenPrecio == OrdenPrecio.MAYOR_A_MENOR,
                                    onClick = {
                                        ordenPrecio = if (ordenPrecio == OrdenPrecio.MAYOR_A_MENOR) OrdenPrecio.NINGUNO else OrdenPrecio.MAYOR_A_MENOR
                                    },
                                    label = { Text("Precio:  ↑") }
                                )
                            }
                        }

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(listaCategorias) { categoria ->
                                ChipCategoria(
                                    texto = categoria,
                                    seleccionado = categoria == categoriaSeleccionada,
                                    onClick = { categoriaSeleccionada = categoria }
                                )
                            }
                        }

                        if (productosFiltrados.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No se encontraron productos",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(vertical = 8.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(productosFiltrados) { producto ->
                                    ProductoCard(
                                        producto = producto,
                                        onClick = { onProductoClick(producto) },
                                        onAgregar = { onAgregarProducto(producto) },
                                        onToggleFavorito = { onToggleFavorito(producto) }
                                    )
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // PESTAÑA 1: CATEGORÍAS
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Categorías de Productos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        listaCategorias.forEach { cat ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        categoriaSeleccionada = cat
                                        pestanaSeleccionada = 0
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = cat,
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = VerdeBodega)
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // PESTAÑA 2: MIS PEDIDOS (HISTORIAL)
                    VistaPedidos(pedidos = pedidos)
                }

                3 -> {
                    // PESTAÑA 3: PERFIL Y MODO OSCURO
                    VistaPerfil(
                        usuario = usuario,
                        modoOscuro = modoOscuro,
                        onToggleModoOscuro = onToggleModoOscuro
                    )
                }
            }
        }
    }
}

@Composable
private fun ChipCategoria(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val fondo = if (seleccionado) VerdeBodega else GrisClaro
    val contenido = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .background(fondo, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(text = texto, color = contenido, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun VistaPedidos(pedidos: List<Pedido>) {
    if (pedidos.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Aún no tienes pedidos confirmados",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pedidos) { pedido ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Pedido #${pedido.id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(pedido.estado, color = VerdeBodega, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Detalle del pedido:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        pedido.items.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${item.cantidad}x ${item.producto.nombre}", style = MaterialTheme.typography.bodyMedium)
                                Text("S/ %.2f".format(item.producto.precio * item.cantidad), style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tipo de envío: ${pedido.tipoEnvio}", fontSize = 12.sp, color = Color.Gray)
                            Text("Total: S/ %.2f".format(pedido.total), fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
                        }

                        if (pedido.direccion.isNotBlank()) {
                            Text("Dirección: ${pedido.direccion}", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VistaPerfil(
    usuario: Usuario?,
    modoOscuro: Boolean,
    onToggleModoOscuro: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(VerdeBodega.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = VerdeBodega, modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(usuario?.nombre ?: "Usuario Invitado", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Ajustes de la App", fontWeight = FontWeight.Bold, color = VerdeBodega)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (modoOscuro) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = null,
                            tint = VerdeBodega
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Modo Oscuro")
                    }
                    Switch(
                        checked = modoOscuro,
                        onCheckedChange = onToggleModoOscuro,
                        colors = SwitchDefaults.colors(checkedThumbColor = VerdeBodega)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Datos de la cuenta", fontWeight = FontWeight.Bold, color = VerdeBodega)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Teléfono: ${if (!usuario?.telefono.isNullOrBlank()) usuario?.telefono else "-"}")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Dirección: ${if (!usuario?.direccion.isNullOrBlank()) usuario?.direccion else "-"}")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Referencia: ${if (!usuario?.referencia.isNullOrBlank()) usuario?.referencia else "-"}")
            }
        }
    }
}

@Composable
private fun BarraInferior(
    seleccionado: Int,
    onSeleccionar: (Int) -> Unit
) {
    val items = listOf(
        Triple("Inicio", Icons.Default.Home, 0),
        Triple("Categorías", Icons.Default.List, 1),
        Triple("Pedidos", Icons.Default.Receipt, 2),
        Triple("Perfil", Icons.Default.Person, 3)
    )
    NavigationBar {
        items.forEach { (etiqueta, icono, indice) ->
            NavigationBarItem(
                selected = seleccionado == indice,
                onClick = { onSeleccionar(indice) },
                icon = { Icon(icono, contentDescription = etiqueta) },
                label = { Text(etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VerdeBodega,
                    selectedTextColor = VerdeBodega
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InicioPreview() {
    BodegaTheme {
        InicioScreen(
            cantidadCarrito = 3,
            onVerCarrito = {},
            onProductoClick = {},
            onAgregarProducto = {}
        )
    }
}