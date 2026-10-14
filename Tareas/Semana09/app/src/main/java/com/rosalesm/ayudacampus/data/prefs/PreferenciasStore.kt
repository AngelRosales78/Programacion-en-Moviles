package com.rosalesm.ayudacampus.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("ajustes")

class PreferenciasStore(private val context: Context) {

    companion object {
        val PERFIL_ACTIVO = stringPreferencesKey("perfil_activo")
        val USUARIO_ID = intPreferencesKey("usuario_id")
        val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        val NOTIFICACIONES = booleanPreferencesKey("notificaciones")
        val RECIBIR_TICKETS = booleanPreferencesKey("recibir_tickets")
    }

    // Lectura: Un Flow por cada preferencia[cite: 13]
    val perfilActivo: Flow<String> = context.dataStore.data.map { it[PERFIL_ACTIVO] ?: "" }
    val usuarioId: Flow<Int> = context.dataStore.data.map { it[USUARIO_ID] ?: -1 }
    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { it[MODO_OSCURO] ?: false }
    val notificaciones: Flow<Boolean> = context.dataStore.data.map { it[NOTIFICACIONES] ?: true }
    val recibirTickets: Flow<Boolean> = context.dataStore.data.map { it[RECIBIR_TICKETS] ?: true }

    // Escritura: Funciones suspend[cite: 13, 14]
    suspend fun guardarSesion(rol: String, usuarioId: Int) {
        context.dataStore.edit { prefs ->
            prefs[PERFIL_ACTIVO] = rol
            prefs[USUARIO_ID] = usuarioId
        }
    }

    suspend fun guardarModoOscuro(activo: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[MODO_OSCURO] = activo
        }
    }

    suspend fun guardarNotificaciones(activo: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[NOTIFICACIONES] = activo
        }
    }

    suspend fun guardarRecibirTickets(activo: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[RECIBIR_TICKETS] = activo
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { prefs ->
            prefs.remove(PERFIL_ACTIVO)
            prefs.remove(USUARIO_ID)
        }
    }
}