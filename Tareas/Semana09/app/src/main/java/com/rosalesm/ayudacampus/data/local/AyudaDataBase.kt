package com.rosalesm.ayudacampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rosalesm.ayudacampus.data.local.entity.Categoria
import com.rosalesm.ayudacampus.data.local.entity.Comentario
import com.rosalesm.ayudacampus.data.local.entity.Ticket
import com.rosalesm.ayudacampus.data.local.entity.Usuario
import com.rosalesm.ayudacampus.data.local.dao.CategoriaDao
import com.rosalesm.ayudacampus.data.local.dao.TicketDao
import com.rosalesm.ayudacampus.data.local.dao.UsuarioDao


@Database(
    entities = [
        Categoria::class,
        Usuario::class,
        Ticket::class,
        Comentario::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AyudaDatabase : RoomDatabase() {

    abstract fun categoriaDao(): CategoriaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun ticketDao(): TicketDao

    companion object {
        @Volatile
        private var INSTANCE: AyudaDatabase? = null

        fun obtener(context: Context): AyudaDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AyudaDatabase::class.java,
                    "ayudacampus.db"
                ).build().also { INSTANCE = it }
            }
    }
}