package com.tecsup.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.tecsup.campusgps.model.Posicion
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(context: Context) : UbicacionRepository {

    private val appContext = context.applicationContext
    private val cliente = LocationServices.getFusedLocationProviderClient(appContext)

    // El permiso se revisa en la pantalla antes de llamar a este método
    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = runCatching {
        val token = CancellationTokenSource()
        val location = cliente
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token.token)
            .await()
            ?: throw UbicacionNoDisponibleException()
        Posicion(location.latitude, location.longitude)
    }

    override fun playServicesDisponible(): Boolean =
        GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(appContext) == ConnectionResult.SUCCESS
}
