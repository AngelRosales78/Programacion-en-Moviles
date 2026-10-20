package com.rosalesm.ayudacampus

import android.app.Application
import com.rosalesm.ayudacampus.data.local.AyudaDatabase
import com.rosalesm.ayudacampus.data.local.DatosIniciales
import com.rosalesm.ayudacampus.data.prefs.PreferenciasStore
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import com.rosalesm.ayudacampus.data.repository.AyudaRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AyudaApp : Application() {

    lateinit var database: AyudaDatabase
        private set

    lateinit var repository: AyudaRepository
        private set

    lateinit var preferenciasStore: PreferenciasStore
        private set

    override fun onCreate() {
        super.onCreate()
        database = AyudaDatabase.obtener(this)
        repository = AyudaRepositoryRoom(
            categoriaDao = database.categoriaDao(),
            usuarioDao = database.usuarioDao(),
            ticketDao = database.ticketDao()
        )
        preferenciasStore = PreferenciasStore(this)

        runBlocking(Dispatchers.IO) {
            DatosIniciales.sembrar(database)
        }
    }
}