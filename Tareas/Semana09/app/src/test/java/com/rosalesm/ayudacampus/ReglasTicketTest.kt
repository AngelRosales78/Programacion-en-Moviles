package com.rosalesm.ayudacampus

import com.rosalesm.ayudacampus.data.local.entity.*
import com.rosalesm.ayudacampus.data.local.relation.TicketConDetalle
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import com.rosalesm.ayudacampus.model.*
import com.rosalesm.ayudacampus.viewmodel.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class ReglasTicketTest {
    private val dispatcher=UnconfinedTestDispatcher()
    @Before fun preparar() { Dispatchers.setMain(dispatcher) }
    @After fun limpiar() { Dispatchers.resetMain() }
    private val alumno=Usuario(1,"Alumno","alumno@campus.test","1234","CLIENTE")
    private val soporte=Usuario(2,"Soporte","soporte@campus.test","admin","ADMIN")
    @Test fun `solo el propietario puede cerrar un resuelto con una a cinco estrellas`() = runTest {
        val repo=FakeRepo(alumno)
        val vm=TicketDetalleViewModel(repo,1,alumno)
        vm.cerrarConCalificacion(4); runCurrent(); assertEquals(0,repo.cierres)
        repo.estado("RESUELTO")
        vm.cerrarConCalificacion(0); vm.cerrarConCalificacion(6); runCurrent(); assertEquals(0,repo.cierres)
        TicketDetalleViewModel(repo,1,alumno.copy(id=99)).cerrarConCalificacion(4); runCurrent(); assertEquals(0,repo.cierres)
        vm.cerrarConCalificacion(4); runCurrent(); assertEquals(1,repo.cierres); assertEquals("CERRADO",repo.detalle.value!!.ticket.estado)
    }
    @Test fun `cerrado no admite comentarios y reabrir limpia calificacion y fecha`() = runTest {
        val repo=FakeRepo(alumno); val vm=TicketDetalleViewModel(repo,1,alumno)
        repo.estado("CERRADO"); vm.agregarComentario("hola"); vm.reabrir(); runCurrent(); assertEquals(0,repo.comentarios); assertEquals("CERRADO",repo.detalle.value!!.ticket.estado)
        repo.estado("RESUELTO"); vm.reabrir(); runCurrent(); assertEquals("ABIERTO",repo.detalle.value!!.ticket.estado); assertNull(repo.detalle.value!!.ticket.calificacion); assertNull(repo.detalle.value!!.ticket.fechaCierre)
        vm.agregarComentario("  "); runCurrent(); assertEquals(0,repo.comentarios)
    }
    @Test fun `soporte no puede cerrar ni modificar un ticket cerrado`() = runTest {
        val repo=FakeRepo(alumno); val vm=AtenderViewModel(repo,1,soporte)
        vm.cambiarEstado(EstadoTicket.CERRADO); vm.guardar(); runCurrent(); assertEquals("ABIERTO",repo.detalle.value!!.ticket.estado)
        repo.estado("CERRADO"); vm.cambiarEstado(EstadoTicket.EN_PROCESO); vm.cambiarPrioridad(Prioridad.ALTA); vm.guardar(); vm.responder("respuesta"); runCurrent()
        assertEquals("CERRADO",repo.detalle.value!!.ticket.estado); assertEquals(0,repo.comentarios)
    }
    @Test fun `categoria referenciada no se elimina y nombre vacio no se guarda`() = runTest {
        val repo=FakeRepo(alumno); val vm=CategoriasViewModel(repo)
        vm.eliminar(repo.categoria); runCurrent(); assertEquals(0,repo.eliminadas); assertNotNull(vm.error.value)
        vm.guardarCategoria(" ","tag",true); runCurrent(); assertEquals(0,repo.guardadas)
    }
}

class FakeRepo(alumno: Usuario) : AyudaRepository {
    val categoria=Categoria(1,"Hardware","pc")
    val detalle=MutableStateFlow<TicketConDetalle?>(TicketConDetalle(Ticket(1,alumno.id,1,"Equipo roto","No funciona el equipo","Lab","MEDIA","ABIERTO",1L),categoria,alumno,emptyList()))
    var cierres=0; var comentarios=0; var eliminadas=0; var guardadas=0
    fun estado(e: String) { detalle.value=detalle.value!!.copy(ticket=detalle.value!!.ticket.copy(estado=e)) }
    override suspend fun login(correo:String,clave:String):Usuario?=null
    override suspend fun obtenerUsuario(id:Int):Usuario?=null
    override suspend fun obtenerUsuarioPorRol(rol:String):Usuario?=null
    override suspend fun registrarUsuario(usuario:Usuario) {}
    override suspend fun categoriaTieneTickets(id:Int)=true
    override fun observarCategorias()=flowOf(listOf(categoria))
    override fun observarCategoriasActivas()=observarCategorias()
    override suspend fun guardarCategoria(c:Categoria) { guardadas++ }
    override suspend fun eliminarCategoria(c:Categoria) { eliminadas++ }
    override suspend fun cambiarActiva(id:Int,activa:Boolean) {}
    override suspend fun crearTicket(clienteId:Int,categoriaId:Int,titulo:String,descripcion:String,ubicacion:String,prioridad:Prioridad)=1L
    override fun observarTicketsCliente(clienteId:Int)=detalle.map { listOfNotNull(it).filter { t -> t.ticket.clienteId==clienteId } }
    override fun observarTodosLosTickets()=detalle.map { listOfNotNull(it) }
    override fun observarTicket(id:Int):Flow<TicketConDetalle?> = detalle
    override suspend fun cambiarEstado(ticketId:Int,estado:EstadoTicket) { estado(estado.name) }
    override suspend fun cambiarPrioridad(ticketId:Int,prioridad:Prioridad) { detalle.value=detalle.value!!.copy(ticket=detalle.value!!.ticket.copy(prioridad=prioridad.name)) }
    override suspend fun cerrarConCalificacion(ticketId:Int,calificacion:Int) { cierres++; detalle.value=detalle.value!!.copy(ticket=detalle.value!!.ticket.copy(estado="CERRADO",calificacion=calificacion,fechaCierre=100L)) }
    override suspend fun reabrir(ticketId:Int) { detalle.value=detalle.value!!.copy(ticket=detalle.value!!.ticket.copy(estado="ABIERTO",calificacion=null,fechaCierre=null)) }
    override suspend fun agregarComentario(ticketId:Int,autor:Usuario,texto:String) { comentarios++ }
    override fun observarPromedioResolucion()=flowOf<Double?>(null)
    override fun observarCantidadPorEstado(estado:EstadoTicket)=flowOf(0)
    override fun observarCantidadPorCategoria()=flowOf(emptyList<ConteoCategoria>())
    override fun observarAltasSinAtender(limite:Long)=flowOf(0)
}
