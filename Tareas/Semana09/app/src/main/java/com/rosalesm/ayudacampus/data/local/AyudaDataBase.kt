package com.rosalesm.ayudacampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rosalesm.ayudacampus.data.local.dao.CategoriaDao
import com.rosalesm.ayudacampus.data.local.dao.TicketDao
import com.rosalesm.ayudacampus.data.local.dao.UsuarioDao
import com.rosalesm.ayudacampus.data.local.entity.Categoria
import com.rosalesm.ayudacampus.data.local.entity.Comentario
import com.rosalesm.ayudacampus.data.local.entity.Ticket
import com.rosalesm.ayudacampus.data.local.entity.Usuario

@Database(
    entities = [Categoria::class, Usuario::class, Ticket::class, Comentario::class],
    version = 2,
    exportSchema = false
)
abstract class AyudaDatabase : RoomDatabase() {

    abstract fun categoriaDao(): CategoriaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun ticketDao(): TicketDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
                    override fun migrate(db: SupportSQLiteDatabase) {
                        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_usuarios_correo ON usuarios(correo)")
                        // Rebuild only the FK definitions; preserve every ticket and comment.
                        db.execSQL("CREATE TABLE tickets_nueva (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, clienteId INTEGER NOT NULL, categoriaId INTEGER NOT NULL, titulo TEXT NOT NULL, descripcion TEXT NOT NULL, ubicacion TEXT NOT NULL, prioridad TEXT NOT NULL, estado TEXT NOT NULL, fecha INTEGER NOT NULL, fechaCierre INTEGER, calificacion INTEGER, FOREIGN KEY(clienteId) REFERENCES usuarios(id) ON UPDATE NO ACTION ON DELETE RESTRICT, FOREIGN KEY(categoriaId) REFERENCES categorias(id) ON UPDATE NO ACTION ON DELETE RESTRICT)")
                        db.execSQL("INSERT INTO tickets_nueva SELECT * FROM tickets")
                        db.execSQL("CREATE TEMP TABLE comentarios_respaldo AS SELECT * FROM comentarios")
                        db.execSQL("DELETE FROM comentarios")
                        db.execSQL("DROP TABLE tickets")
                        db.execSQL("ALTER TABLE tickets_nueva RENAME TO tickets")
                        db.execSQL("CREATE INDEX index_tickets_clienteId ON tickets(clienteId)")
                        db.execSQL("CREATE INDEX index_tickets_categoriaId ON tickets(categoriaId)")
                        db.execSQL("INSERT INTO comentarios SELECT * FROM comentarios_respaldo")
                        db.execSQL("DROP TABLE comentarios_respaldo")
                    }
                }

        @Volatile
        private var INSTANCE: AyudaDatabase? = null

        fun obtener(context: Context): AyudaDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AyudaDatabase::class.java,
                "ayudacampus.db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
                .also { INSTANCE = it }
        }
    }
}