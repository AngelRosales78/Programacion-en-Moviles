package com.rosalesm.ayudacampus.data.repository

import com.rosalesm.ayudacampus.data.local.dao.CategoriaDao
import com.rosalesm.ayudacampus.data.local.dao.TicketDao
import com.rosalesm.ayudacampus.data.local.dao.UsuarioDao
import com.rosalesm.ayudacampus.data.local.entity.Categoria
import com.rosalesm.ayudacampus.data.local.entity.Comentario
import com.rosalesm.ayudacampus.data.local.entity.Ticket
import com.rosalesm.ayudacampus.data.local.entity.Usuario
import com.rosalesm.ayudacampus.data.local.relation.TicketConDetalle
import com.rosalesm.ayudacampus.model.ConteoCategoria
import com.rosalesm.ayudacampus.model.EstadoTicket
import com.rosalesm.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.Flow

class AyudaRepositoryRoom(
    private val categoriaDao: CategoriaDao,
    private val usuarioDao: UsuarioDao,
    private val ticketDao: TicketDao
) : AyudaRepository {

    override suspend fun obtenerUsuario(id: Int) = usuarioDao.obtener(id)
    override suspend fun obtenerUsuarioPorRol(rol: String) = usuarioDao.obtenerPorRol(rol)
    override suspend fun registrarUsuario(usuario: Usuario) {
        require(usuarioDao.buscarCorreo(usuario.correo) == null) { "Este correo ya está registrado" }
        usuarioDao.insertar(usuario)
    }
    override suspend fun categoriaTieneTickets(id: Int) = ticketDao.contarCategoria(id) > 0

    // --- USUARIOS ---
    override suspend fun login(correo: String, clave: String): Usuario? {
        return usuarioDao.login(correo, clave)
    }

    // --- CATEGORÍAS ---
    override fun observarCategorias(): Flow<List<Categoria>> {
        return categoriaDao.observarTodas()
    }

    override fun observarCategoriasActivas(): Flow<List<Categoria>> {
        return categoriaDao.observarActivas()
    }

    override suspend fun guardarCategoria(c: Categoria) {
        if (c.id == 0) categoriaDao.insertar(c) else categoriaDao.actualizar(c)
    }

    override suspend fun eliminarCategoria(c: Categoria) {
        require(!categoriaTieneTickets(c.id)) { "No se puede eliminar una categoría con tickets" }
        categoriaDao.eliminar(c)
    }

    override suspend fun cambiarActiva(id: Int, activa: Boolean) {
        categoriaDao.cambiarActiva(id, activa)
    }

    // --- TICKETS ---
    override suspend fun crearTicket(
        clienteId: Int,
        categoriaId: Int,
        titulo: String,
        descripcion: String,
        ubicacion: String,
        prioridad: Prioridad
    ): Long {
        val nuevoTicket = Ticket(
            clienteId = clienteId,
            categoriaId = categoriaId,
            titulo = titulo,
            descripcion = descripcion,
            ubicacion = ubicacion,
            prioridad = prioridad.name,
            estado = EstadoTicket.ABIERTO.name,
            fecha = System.currentTimeMillis()
        )
        return ticketDao.insertarTicket(nuevoTicket)
    }

    override fun observarTicketsCliente(clienteId: Int): Flow<List<TicketConDetalle>> {
        return ticketDao.observarPorCliente(clienteId)
    }

    override fun observarTodosLosTickets(): Flow<List<TicketConDetalle>> {
        return ticketDao.observarTodos()
    }

    override fun observarTicket(id: Int): Flow<TicketConDetalle?> {
        return ticketDao.observarUno(id)
    }

    override suspend fun cambiarEstado(ticketId: Int, estado: EstadoTicket) {
        ticketDao.actualizarEstado(ticketId, estado.name)
    }

    override suspend fun cambiarPrioridad(ticketId: Int, prioridad: Prioridad) {
        ticketDao.actualizarPrioridad(ticketId, prioridad.name)
    }

    override suspend fun cerrarConCalificacion(ticketId: Int, calificacion: Int) {
        ticketDao.cerrar(ticketId, calificacion, System.currentTimeMillis())
    }

    override suspend fun reabrir(ticketId: Int) {
        ticketDao.reabrir(ticketId)
    }

    override suspend fun agregarComentario(ticketId: Int, autor: Usuario, texto: String) {
        val nuevoComentario = Comentario(
            ticketId = ticketId,
            autorId = autor.id,
            autorNombre = autor.nombre,
            autorRol = autor.rol,
            texto = texto,
            fecha = System.currentTimeMillis()
        )
        ticketDao.insertarComentario(nuevoComentario)
    }

    // --- PANEL DE SOPORTE ---
    override fun observarCantidadPorEstado(estado: EstadoTicket): Flow<Int> {
        return ticketDao.contarPorEstado(estado.name)
    }

    override fun observarCantidadPorCategoria(): Flow<List<ConteoCategoria>> {
        return ticketDao.contarPorCategoria()
    }

    override fun observarAltasSinAtender(limite: Long): Flow<Int> {
        return ticketDao.contarAltasSinAtender(limite)
    }
}