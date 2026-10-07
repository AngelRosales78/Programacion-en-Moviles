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
    val tipoEnvio: String,
    val direccion: String,
    val referencia: String,
    val fecha: String = "Hoy",
    val estado: String = "En preparación"
)

val listaCategorias = listOf("Todos", "Bebidas", "Abarrotes", "Snacks", "Otros")

val listaProductosFake = listOf(
    Producto(1, "Arroz Costeño", "Arroz extra, grano largo, ideal para el día a día.", 4.50, "Abarrotes", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-J1_Q3D0c9_D05XbBAWm-Hvw0cTevKSx_b8_Q_0yrqw&s"),
    Producto(2, "Aceite Primor", "Aceite vegetal 1 L, alto en vitamina E.", 8.90, "Abarrotes", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSkx-vgrq5qJBM8KuHqEykCJCW2CxKOTp-bHcjzBfW9Xd_ShXCYLBHUNqA&s=10"),
    Producto(3, "Leche Gloria", "Leche evaporada entera 1 L.", 5.20, "Abarrotes", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRLDPnlDEQz5U98aFqk5Ys2hnRaT9Uj7X9fRm-qEL5c9mbM1jn9Y0z97hI&s=10"),
    Producto(4, "Galleta Oreo", "Galletas de chocolate rellenas 126 g.", 3.50, "Snacks", "https://media.falabella.com/tottusPE/43331113_6/w=1500,h=1500,fit=cover"),
    Producto(5, "Coca-Cola Original", "Bebida gaseosa sabor cola.", 6.50, "Bebidas", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRogDuIdx4r3t2Q4JQ2Gb1kkfck5IGvnAaQIQ2EIFqHocW5YyCK8BAKKA4&s=10"),
    Producto(6, "Inka-Cola", "Bebida gasificada 500ml", 3.00, "Bebidas", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTan2gXLI4CNSpY7EOG1T6cjTtJfrintKDiGHz6gC72gAT6gLtnrPhlvMia&s=10")
)