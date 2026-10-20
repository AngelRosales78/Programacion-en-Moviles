package com.rosalesm.ayudacampus.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "ajustes")

class PreferenciasStore(private val context: Context) {

    companion object {
        val PERFIL_ACTIVO = stringPreferencesKey("perfil_activo") // "CLIENTE" | "ADMIN" | ""
        val USUARIO_ID = intPreferencesKey("usuario_id")
        val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        val NOTIFICACIONES = booleanPreferencesKey("notificaciones")
        val RECIBIR_TICKETS = booleanPreferencesKey("recibir_tickets")
    }

    val perfilActivo: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PERFIL_ACTIVO] ?: ""
    }

    val usuarioId: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[USUARIO_ID] ?: 0
    }

    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[MODO_OSCURO] ?: false
    }

    val notificaciones: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[NOTIFICACIONES] ?: true
    }

    val recibirTickets: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[RECIBIR_TICKETS] ?: true
    }

    suspend fun guardarSesion(rol: String, id: Int) {
        context.dataStore.edit { prefs ->
            prefs[PERFIL_ACTIVO] = rol
            prefs[USUARIO_ID] = id
        }
    }

    suspend fun guardarModoOscuro(activado: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[MODO_OSCURO] = activado
        }
    }

    suspend fun guardarNotificaciones(activado: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[NOTIFICACIONES] = activado
        }
    }

    suspend fun guardarRecibirTickets(activado: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[RECIBIR_TICKETS] = activado
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { prefs ->
            prefs[PERFIL_ACTIVO] = ""
            prefs[USUARIO_ID] = 0
        }
    }
}