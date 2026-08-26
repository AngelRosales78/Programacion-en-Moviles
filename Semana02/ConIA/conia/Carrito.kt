package conia

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
) {
    val subtotal: Double
        get() = precio * cantidad
}

fun main() {
    println("========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP   ")
    println("========================================\n")

    print("Ingrese el nombre del cliente: ")
    val nombreCliente = (readlnOrNull() ?: "").ifBlank { "Cliente Anónimo" }

    val carrito = mutableListOf<Producto>()

    println("\n--- Registro de Productos ---")
    print("¿Cuántos productos desea agregar al carrito?: ")
    val cantidadProductos = (readlnOrNull() ?: "").toIntOrNull() ?: 0

    for (i in 1..cantidadProductos) {
        println("\nProducto #$i:")

        print("  Nombre: ")
        val nombre = (readlnOrNull() ?: "").ifBlank { "Producto $i" }

        print("  Precio (S/): ")
        val precio = (readlnOrNull() ?: "").toDoubleOrNull() ?: 0.0

        print("  Cantidad: ")
        val cantidad = (readlnOrNull() ?: "").toIntOrNull() ?: 1

        carrito.add(Producto(nombre, precio, cantidad))
    }

    println("\n========================================")
    println("RESUMEN DE LA COMPRA")
    println("Cliente: $nombreCliente\n")

    // Procesamiento funcional del carrito
    carrito.forEach { producto ->
        println("Producto agregado: ${producto.nombre} | Cantidad: ${producto.cantidad} | Subtotal: S/ %.2f".format(producto.subtotal))
    }

    mostrarDetalle(carrito)
}

fun mostrarDetalle(productos: List<Producto>) {
    val subtotal = productos.sumOf { it.subtotal }
    val igv = subtotal * 0.18
    val total = subtotal + igv

    println("\n----------------------------------------")
    println("Subtotal : S/ %.2f".format(subtotal))
    println("IGV (18%%): S/ %.2f".format(igv))
    println("Total    : S/ %.2f".format(total))
    println("----------------------------------------")
}