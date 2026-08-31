package com.rosalesm.prestamolibros

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    val formatterEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formatterTabla = DateTimeFormatter.ofPattern("dd/MM")
    val DIAS_PRESTAMO_PERMITIDOS = 7L

    println("==========================================")
    println("   SISTEMA DE PRESTAMOS DE BIBLIOTECA")
    println("==========================================")

    // 1. Entrada de datos
    print("\nIngrese el titulo del libro: ")
    val titulo = scanner.nextLine()

    println("\nSeleccione el tipo de usuario:")
    println("1. Alumno (Tarifa base: S/ 1.50)")
    println("2. Docente (Tarifa base: S/ 3.00)")
    print("Opcion (1 o 2): ")
    val opcionUsuario = scanner.nextInt()
    scanner.nextLine()

    val (tipoUsuario, tarifaBase, multaDiaria) = when (opcionUsuario) {
        1 -> Triple("Alumno", 1.50, 1.50)
        2 -> Triple("Docente", 3.00, 3.00)
        else -> Triple("Alumno", 1.50, 1.50)
    }

    // 2. Registro de fechas
    println("\n--- Registro de Fechas (Formato: DD/MM/YYYY) ---")
    val fechaPrestamo = pedirFecha(scanner, formatterEntrada, "Fecha de prestamo (ej. 01/01/2026): ")

    val fechaPactada = fechaPrestamo.plusDays(DIAS_PRESTAMO_PERMITIDOS)
    println("-> Fecha limite asignada automaticamente: ${fechaPactada.format(formatterEntrada)}")

    val fechaEntrega = pedirFecha(scanner, formatterEntrada, "Fecha real de entrega (ej. 18/01/2026): ")

    // 3. Calculo de atraso
    val diasRetrasoCalculados = ChronoUnit.DAYS.between(fechaPactada, fechaEntrega)
    val diasRetraso = if (diasRetrasoCalculados > 0) diasRetrasoCalculados.toInt() else 0

    // 4. Salida por terminal
    println("\n==========================================")
    println("           DETALLE DE MORAS")
    println("==========================================")
    println("Libro        : $titulo")
    println("Tipo Usuario : $tipoUsuario")
    println("Tarifa Base  : S/ %.2f".format(tarifaBase))
    println("------------------------------------------")

    if (diasRetraso > 0) {
        println("+------+-------+---------------+-----------+")
        println("| Dia  | Fecha | Multa por dia | Acumulado |")
        println("+------+-------+---------------+-----------+")

        var acumulado = 0.0

        for (i in 1..diasRetraso) {
            val fechaMora = fechaPactada.plusDays(i.toLong())
            acumulado += multaDiaria

            println("| %-4d | %-5s | %-13.2f | %-9.2f |".format(
                i,
                fechaMora.format(formatterTabla),
                multaDiaria,
                acumulado
            ))
        }

        println("+------+-------+---------------+-----------+")
        println("\nMULTA TOTAL S/ %.2f".format(acumulado))
    } else {
        println("Estado: Devuelto a tiempo. No registra multas.")
        println("TOTAL A PAGAR: S/ %.2f".format(tarifaBase))
    }

    println("==========================================")
}

fun pedirFecha(scanner: Scanner, formatter: DateTimeFormatter, mensaje: String): LocalDate {
    while (true) {
        print(mensaje)
        val entrada = scanner.nextLine()
        try {
            return LocalDate.parse(entrada, formatter)
        } catch (e: Exception) {
            println(" Error: Formato incorrecto. Use DD/MM/YYYY.")
        }
    }
}