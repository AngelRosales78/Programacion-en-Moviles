package com.tecsup.mibodega.ui.cliente.modelo

data class Usuario(
    val nombre: String = "",
    val telefono: String = "",
    val direccion: String = "",
    val referencia: String = ""
)

data class Pedido(
    val id: Int,
    val items: List<ItemCarrito>,
    val subtotal: Double,
    val costoEnvio: Double,
    val total: Double,
    val tipoEnvio: String, // "Delivery" o "Recojo en tienda"
    val direccion: String,
    val referencia: String,
    val fecha: String = "Hoy",
    val estado: String = "En preparación"
)

val listaCategorias = listOf("Todos", "Bebidas", "Abarrotes", "Snacks", "Otros")

val listaProductosFake = listOf(
    Producto(1, "Arroz Costeño", "Arroz extra, grano largo, ideal para el día a día.", 4.50, "Abarrotes"),
    Producto(2, "Aceite Primor", "Aceite vegetal 1 L, alto en vitamina E.", 8.90, "Abarrotes"),
    Producto(3, "Leche Gloria", "Leche evaporada entera 1 L.", 5.20, "Abarrotes"),
    Producto(4, "Galleta Oreo", "Galletas de chocolate rellenas 126 g.", 3.50, "Snacks"),
    Producto(5, "Coca-Cola Original", "Bebida gaseosa sabor cola.", 6.50, "Bebidas"),
    Producto(6, "Inka-Cola", "Bebida gasificada 500ml", 3.00, "Bebidas")
)