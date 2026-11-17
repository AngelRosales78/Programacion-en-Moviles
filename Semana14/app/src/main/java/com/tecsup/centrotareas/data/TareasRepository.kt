package com.tecsup.centrotareas.data

import kotlinx.coroutines.flow.Flow

interface TareasRepository {
    /** Mejora IA: la tarea simulada emite su avance (0f a 1f) como un Flow. */
    fun progresoTarea(): Flow<Float>
    /** Mejora IA: [alAvanzar] recibe el avance (0f a 1f) cada 1 % del recorrido. */
    suspend fun contarPrimos(hasta: Int, alAvanzar: (Float) -> Unit = {}): Int
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
}
