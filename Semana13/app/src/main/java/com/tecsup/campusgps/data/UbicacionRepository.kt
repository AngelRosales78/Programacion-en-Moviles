package com.tecsup.campusgps.data

import com.tecsup.campusgps.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>

    // Paso 8: el ViewModel pregunta sin necesitar un Context
    fun playServicesDisponible(): Boolean
}

// Se lanza cuando getCurrentLocation devuelve null (GPS apagado o sin ubicación)
class UbicacionNoDisponibleException :
    Exception("No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo")
