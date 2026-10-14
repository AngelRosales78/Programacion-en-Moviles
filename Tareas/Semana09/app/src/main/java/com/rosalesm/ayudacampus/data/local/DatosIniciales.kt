package com.rosalesm.ayudacampus.data.local


import com.rosalesm.ayudacampus.data.local.entity.Categoria
import com.rosalesm.ayudacampus.data.local.entity.Comentario
import com.rosalesm.ayudacampus.data.local.entity.Ticket
import com.rosalesm.ayudacampus.data.local.entity.Usuario
import com.rosalesm.ayudacampus.model.EstadoTicket
import com.rosalesm.ayudacampus.model.Prioridad
import com.rosalesm.ayudacampus.model.Rol

object DatosIniciales {

    suspend fun sembrar(db: AyudaDatabase) {
        if (db.usuarioDao().contar() == 0) {
            // 1. Categorías iniciales
            val categorias = listOf(
                Categoria(id = 1, nombre = "Hardware", icono = "pc", activa = true),
                Categoria(id = 2, nombre = "Software", icono = "code", activa = true),
                Categoria(id = 3, nombre = "Red / Wi-Fi", icono = "wifi", activa = true),
                Categoria(id = 4, nombre = "Cuenta", icono = "key", activa = true),
                Categoria(id = 5, nombre = "Impresión", icono = "printer", activa = true)
            )
            db.categoriaDao().insertarTodas(categorias)

            // 2. Usuarios de prueba
            val estudiante = Usuario(
                id = 1,
                nombre = "Juan Pérez",
                correo = "juan@campus.test",
                clave = "1234",
                rol = Rol.CLIENTE.name
            )
            val soporte = Usuario(
                id = 2,
                nombre = "Soporte TI",
                correo = "soporte@campus.test",
                clave = "admin",
                rol = Rol.ADMIN.name
            )
            db.usuarioDao().insertarTodas(listOf(estudiante, soporte))

            // 3. Tickets de ejemplo con distintas fechas
            val ahora = System.currentTimeMillis()
            val hora = 3600 * 1000L

            val tickets = listOf(
                Ticket(
                    id = 12,
                    clienteId = 1,
                    categoriaId = 3,
                    titulo = "Sin Wi-Fi en el laboratorio 3",
                    descripcion = "Desde ayer no hay señal de Wi-Fi en todo el laboratorio. Afecta a 30 alumnos.",
                    ubicacion = "Lab 3 - Pabellón B",
                    prioridad = Prioridad.ALTA.name,
                    estado = EstadoTicket.EN_PROCESO.name,
                    fecha = ahora - (2 * hora)
                ),
                Ticket(
                    id = 11,
                    clienteId = 1,
                    categoriaId = 1,
                    titulo = "Teclado no responde",
                    descripcion = "El teclado de la PC 12 en el aula 204 no enciende ni responde.",
                    ubicacion = "Aula 204",
                    prioridad = Prioridad.MEDIA.name,
                    estado = EstadoTicket.ABIERTO.name,
                    fecha = ahora - (5 * hora)
                ),
                Ticket(
                    id = 9,
                    clienteId = 1,
                    categoriaId = 5,
                    titulo = "No puedo imprimir en biblioteca",
                    descripcion = "Envío el documento a la impresora principal pero no sale la cola de impresión.",
                    ubicacion = "Biblioteca",
                    prioridad = Prioridad.BAJA.name,
                    estado = EstadoTicket.RESUELTO.name,
                    fecha = ahora - (24 * hora)
                )
            )

            for (t in tickets) {
                db.ticketDao().insertarTicket(t)
            }

            // 4. Comentarios iniciales
            val comentarios = listOf(
                Comentario(
                    ticketId = 12,
                    autorId = 2,
                    autorNombre = "Soporte TI",
                    autorRol = Rol.ADMIN.name,
                    texto = "Revisamos el access point del pabellón B hoy.",
                    fecha = ahora - (1 * hora)
                ),
                Comentario(
                    ticketId = 12,
                    autorId = 1,
                    autorNombre = "Juan Pérez",
                    autorRol = Rol.CLIENTE.name,
                    texto = "Gracias, quedo atento.",
                    fecha = ahora - (30 * 60 * 1000L)
                )
            )

            for (c in comentarios) {
                db.ticketDao().insertarComentario(c)
            }
        }
    }
}