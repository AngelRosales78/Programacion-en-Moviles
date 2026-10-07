package com.tecsup.mibodega.ui.cliente

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.*
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen
import com.tecsup.mibodega.ui.theme.BodegaTheme

object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val DETALLE = "detalle/{productoId}"
    const val CARRITO = "carrito"
    const val ENTREGA = "entrega"
    const val CONFIRMACION = "confirmacion"

    fun detalle(productoId: Int) = "detalle/$productoId"
}

@Composable
fun ClienteApp() {
    val navController = rememberNavController()

    // ESTADOS GLOBALES DE LA APLICACIÓN
    var modoOscuro by remember { mutableStateOf(false) }
    var usuarioActual by remember { mutableStateOf<Usuario?>(null) }
    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }
    var idsFavoritos by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var historialPedidos by remember { mutableStateOf<List<Pedido>>(emptyList()) }
    var esDelivery by remember { mutableStateOf(true) } // true = Delivery (S/4.00), false = Recojo (S/0.00)
    var ultimoPedidoRealizado by remember { mutableStateOf<Pedido?>(null) }

    val costoEnvio = if (esDelivery) 4.00 else 0.00

    // Modifica la lista de productos agregando la propiedad de esFavorito dinámicamente
    val productosConFavoritos = remember(idsFavoritos) {
        listaProductosFake.map { producto ->
            producto.copy(esFavorito = idsFavoritos.contains(producto.id))
        }
    }

    BodegaTheme(darkTheme = modoOscuro) {
        NavHost(
            navController = navController,
            startDestination = Rutas.BIENVENIDA,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(350))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(350))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(350))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(350))
            }
        ) {
            // PANTALLA 1: BIENVENIDA & LOGIN
            composable(Rutas.BIENVENIDA) {
                BienvenidaScreen(
                    onIniciarSesion = {
                        usuarioActual = Usuario(
                            nombre = "Cliente Fijo",
                            telefono = "987654321",
                            direccion = "Av. Principal 123",
                            referencia = "Frente a la plaza"
                        )
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                        }
                    },
                    onRegistrarse = { navController.navigate(Rutas.REGISTRO) }
                )
            }

            // PANTALLA 2: REGISTRO DE CUENTA
            composable(Rutas.REGISTRO) {
                RegistroScreen(
                    onVolver = { navController.popBackStack() },
                    onCrearCuentaExitoso = { nombre, telefono, direccion, referencia ->
                        usuarioActual = Usuario(nombre, telefono, direccion, referencia)
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                        }
                    }
                )
            }

            // PANTALLA 3: INICIO & CATÁLOGO
            composable(Rutas.INICIO) {
                InicioScreen(
                    productos = productosConFavoritos,
                    cantidadCarrito = carrito.sumOf { it.cantidad },
                    usuario = usuarioActual,
                    pedidos = historialPedidos,
                    modoOscuro = modoOscuro,
                    onToggleModoOscuro = { modoOscuro = it },
                    onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                    onProductoClick = { producto -> navController.navigate(Rutas.detalle(producto.id)) },
                    onAgregarProducto = { producto -> carrito = agregarOSumarProducto(carrito, producto, 1) },
                    onToggleFavorito = { producto ->
                        idsFavoritos = if (idsFavoritos.contains(producto.id)) {
                            idsFavoritos - producto.id
                        } else {
                            idsFavoritos + producto.id
                        }
                    }
                )
            }

            // PANTALLA 4: DETALLE DE PRODUCTO
            composable(
                route = Rutas.DETALLE,
                arguments = listOf(navArgument("productoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
                val producto = productosConFavoritos.first { it.id == productoId }

                DetalleProductoScreen(
                    producto = producto,
                    onVolver = { navController.popBackStack() },
                    onAgregarAlCarrito = { prod, cant ->
                        carrito = agregarOSumarProducto(carrito, prod, cant)
                        navController.popBackStack()
                    }
                )
            }

            // PANTALLA 5: CARRITO DE COMPRAS
            composable(Rutas.CARRITO) {
                CarritoScreen(
                    carrito = carrito,
                    esDelivery = esDelivery,
                    costoEnvio = costoEnvio,
                    onCambiarTipoEnvio = { esDelivery = it },
                    onVolver = { navController.popBackStack() },
                    onIncrementar = { producto ->
                        carrito = carrito.map {
                            if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                        }
                    },
                    onDecrementar = { producto ->
                        carrito = carrito.mapNotNull {
                            when {
                                it.producto.id != producto.id -> it
                                it.cantidad > 1 -> it.copy(cantidad = it.cantidad - 1)
                                else -> null
                            }
                        }
                    },
                    onEliminar = { producto ->
                        carrito = carrito.filterNot { it.producto.id == producto.id }
                    },
                    onContinuarPedido = {
                        navController.navigate(Rutas.ENTREGA)
                    }
                )
            }

            // PANTALLA 6: DATOS DE ENTREGA & PAGO
            composable(Rutas.ENTREGA) {
                DatosEntregaScreen(
                    usuario = usuarioActual,
                    esDelivery = esDelivery,
                    onVolver = { navController.popBackStack() },
                    onConfirmarPedido = { dir, ref ->
                        val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
                        val totalCalculado = subtotal + costoEnvio

                        val nuevoPedido = Pedido(
                            id = 1000 + historialPedidos.size + 1,
                            items = carrito,
                            subtotal = subtotal,
                            costoEnvio = costoEnvio,
                            total = totalCalculado,
                            tipoEnvio = if (esDelivery) "Delivery" else "Recojo en tienda",
                            direccion = dir,
                            referencia = ref
                        )

                        ultimoPedidoRealizado = nuevoPedido
                        historialPedidos = listOf(nuevoPedido) + historialPedidos

                        navController.navigate(Rutas.CONFIRMACION)
                    }
                )
            }

            // PANTALLA 7: CONFIRMACIÓN DE PEDIDO
            composable(Rutas.CONFIRMACION) {
                ConfirmacionScreen(
                    total = ultimoPedidoRealizado?.total ?: 0.0,
                    direccion = ultimoPedidoRealizado?.direccion ?: "",
                    referencia = ultimoPedidoRealizado?.referencia ?: "",
                    onVolverAlInicio = {
                        carrito = emptyList()
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.INICIO) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

private fun agregarOSumarProducto(
    carrito: List<ItemCarrito>,
    producto: Producto,
    cantidad: Int
): List<ItemCarrito> {
    val itemExistente = carrito.find { it.producto.id == producto.id }
    return if (itemExistente != null) {
        carrito.map {
            if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + cantidad) else it
        }
    } else {
        carrito + ItemCarrito(producto = producto, cantidad = cantidad)
    }
}