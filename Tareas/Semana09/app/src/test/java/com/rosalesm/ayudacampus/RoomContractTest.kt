package com.rosalesm.ayudacampus
import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rosalesm.ayudacampus.data.local.*
import com.rosalesm.ayudacampus.data.local.entity.*
import com.rosalesm.ayudacampus.data.repository.AyudaRepositoryRoom
import com.rosalesm.ayudacampus.model.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk=[34],application=Application::class)
class RoomContractTest {
    private lateinit var db: AyudaDatabase
    private lateinit var repo: AyudaRepositoryRoom
    @Before fun preparar() = runBlocking {
        db=Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),AyudaDatabase::class.java).allowMainThreadQueries().build()
        DatosIniciales.sembrar(db)
        repo=AyudaRepositoryRoom(db.categoriaDao(),db.usuarioDao(),db.ticketDao())
    }
    @After fun cerrar() { db.close() }
    @Test fun semillaYPanel() = runBlocking {
        DatosIniciales.sembrar(db)
        assertEquals(2,db.usuarioDao().contar())
        assertEquals(5,repo.observarCategorias().first().size)
        val lista=repo.observarTodosLosTickets().first()
        assertEquals(6,lista.size)
        assertEquals(listOf("ALTA","ALTA","MEDIA","MEDIA","BAJA","BAJA"),lista.map { it.ticket.prioridad })
        assertEquals(3,repo.observarCantidadPorEstado(EstadoTicket.ABIERTO).first())
        assertEquals(1,repo.observarAltasSinAtender(System.currentTimeMillis()-3_600_000L).first())
        assertEquals(6,repo.observarCantidadPorCategoria().first().sumOf { it.cantidad })
        assertEquals(86_400_000.0,repo.observarPromedioResolucion().first()!!,1.0)
        assertNull(repo.login("juan@campus.test","incorrecta"))
        assertNotNull(repo.login("JUAN@campus.test","1234"))
    }
    @Test fun cicloTicket() = runBlocking {
        val juan=repo.login("juan@campus.test","1234")!!
        repo.registrarUsuario(Usuario(nombre="Otra estudiante",correo="otra@campus.test",clave="1234",rol="CLIENTE"))
        val otra=repo.login("otra@campus.test","1234")!!
        val id=repo.crearTicket(otra.id,1,"Equipo averiado","El equipo no enciende","Lab 4",Prioridad.ALTA).toInt()
        assertTrue(repo.observarTicketsCliente(juan.id).first().none { it.ticket.id==id })
        assertEquals(id,repo.observarTicketsCliente(otra.id).first().first().ticket.id)
        repo.agregarComentario(id,repo.obtenerUsuarioPorRol("ADMIN")!!,"Estamos revisando")
        repo.cambiarEstado(id,EstadoTicket.RESUELTO)
        assertEquals("Estamos revisando",repo.observarTicket(id).first()!!.comentarios.single().texto)
        repo.cerrarConCalificacion(id,4)
        val cerrado=repo.observarTicket(id).first()!!.ticket
        assertEquals("CERRADO",cerrado.estado); assertEquals(4,cerrado.calificacion); assertNotNull(cerrado.fechaCierre)
        repo.reabrir(id)
        val abierto=repo.observarTicket(id).first()!!.ticket
        assertEquals("ABIERTO",abierto.estado); assertNull(abierto.calificacion); assertNull(abierto.fechaCierre)
        val hardware=repo.observarCategorias().first().first { it.id==1 }
        try { repo.eliminarCategoria(hardware); fail("No debe eliminar tickets en cascada") } catch(expected: IllegalArgumentException) { }
        assertNotNull(repo.observarTicket(id).first())
        repo.cambiarActiva(1,false)
        assertTrue(repo.observarCategoriasActivas().first().none { it.id==1 })
        try { repo.registrarUsuario(otra.copy(id=0)); fail("Correo duplicado") } catch(expected: IllegalArgumentException) { }
    }

    @Test fun migracion() = runBlocking {
        val context=ApplicationProvider.getApplicationContext<Application>()
        val nombre="prueba-migracion.db"
        context.deleteDatabase(nombre)
        val vieja=context.openOrCreateDatabase(nombre,0,null)
        vieja.execSQL("CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,nombre TEXT NOT NULL,correo TEXT NOT NULL,clave TEXT NOT NULL,rol TEXT NOT NULL)")
        vieja.execSQL("CREATE TABLE categorias (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,nombre TEXT NOT NULL,icono TEXT NOT NULL,activa INTEGER NOT NULL)")
        vieja.execSQL("CREATE TABLE tickets (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,clienteId INTEGER NOT NULL,categoriaId INTEGER NOT NULL,titulo TEXT NOT NULL,descripcion TEXT NOT NULL,ubicacion TEXT NOT NULL,prioridad TEXT NOT NULL,estado TEXT NOT NULL,fecha INTEGER NOT NULL,fechaCierre INTEGER,calificacion INTEGER,FOREIGN KEY(clienteId) REFERENCES usuarios(id) ON DELETE CASCADE,FOREIGN KEY(categoriaId) REFERENCES categorias(id) ON DELETE CASCADE)")
        vieja.execSQL("CREATE INDEX index_tickets_clienteId ON tickets(clienteId)")
        vieja.execSQL("CREATE INDEX index_tickets_categoriaId ON tickets(categoriaId)")
        vieja.execSQL("CREATE TABLE comentarios (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,ticketId INTEGER NOT NULL,autorId INTEGER NOT NULL,autorNombre TEXT NOT NULL,autorRol TEXT NOT NULL,texto TEXT NOT NULL,fecha INTEGER NOT NULL,FOREIGN KEY(ticketId) REFERENCES tickets(id) ON DELETE CASCADE)")
        vieja.execSQL("CREATE INDEX index_comentarios_ticketId ON comentarios(ticketId)")
        vieja.execSQL("INSERT INTO usuarios VALUES(1,'Juan','juan@campus.test','1234','CLIENTE')")
        vieja.execSQL("INSERT INTO categorias VALUES(1,'Hardware','pc',1)")
        vieja.execSQL("INSERT INTO tickets VALUES(1,1,1,'Equipo roto','No funciona el equipo','Lab','ALTA','CERRADO',100,200,4)")
        vieja.execSQL("INSERT INTO comentarios VALUES(1,1,1,'Juan','CLIENTE','Mi historial',150)")
        vieja.version=1; vieja.close()
        val nueva=Room.databaseBuilder(context,AyudaDatabase::class.java,nombre).addMigrations(AyudaDatabase.MIGRATION_1_2).allowMainThreadQueries().build()
        try {
            val detalle=nueva.ticketDao().observarUno(1).first()!!
            assertEquals(4,detalle.ticket.calificacion)
            assertEquals("Mi historial",detalle.comentarios.single().texto)
            assertEquals("Juan",detalle.cliente.nombre)
            try { nueva.categoriaDao().eliminar(detalle.categoria); fail("FK debe proteger el historial") } catch(expected: android.database.sqlite.SQLiteConstraintException) { }
        } finally { nueva.close(); context.deleteDatabase(nombre) }
    }
}
