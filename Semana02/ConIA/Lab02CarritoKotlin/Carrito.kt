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
    println("=========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP    ")
    println("=========================================")

    val nombreCliente = "Angel Rosales"

    val carrito = mutableListOf(
        Producto("Laptop HP", 2500.0, 1),
        Producto("Mouse Logitech", 45.5, 2),
        Producto("Audifonos Sony", 165.0, 1),
        Producto("Teclado Redragon", 120.0, 1)
    )

    println("Cliente: $nombreCliente\n")

    carrito.forEach { println("Producto agregado: ${it.nombre}") }

    mostrarDetalle(carrito)

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println("""
        
        Subtotal : S/ ${String.format("%.2f", subtotal)}
        IGV (18%): S/ ${String.format("%.2f", igv)}
        TOTAL    : S/ ${String.format("%.2f", total)}
    """.trimIndent())

    carrito.maxByOrNull { it.precio }?.let { masCaro ->
        println(String.format("\nProducto mas caro: %s (S/%.2f)", masCaro.nombre, masCaro.precio))
    }

    val descuento = calcularDescuento(total)
    val totalConDescuento = total - descuento

    println(String.format("Descuento: S/ %.2f", descuento))
    println(String.format("TOTAL A PAGAR: S/ %.2f", totalConDescuento))
}


fun calcularSubtotal(productos: List<Producto>): Double = productos.sumOf { it.subtotal }

fun calcularIGV(subtotal: Double): Double = subtotal * 0.18

fun calcularTotal(subtotal: Double, igv: Double): Double = subtotal + igv

fun mostrarDetalle(productos: List<Producto>) {
    println("\n--------- DETALLE DEL CARRITO --------")
    productos.forEachIndexed { i, p ->
        println(String.format("%d. %-20s x%d S/ %8.2f", i + 1, p.nombre, p.cantidad, p.subtotal))
    }
}

fun calcularDescuento(total: Double): Double = when {
    total > 5000 -> total * 0.10
    total > 3000 -> total * 0.05
    else -> 0.0
}