package com.rosalesm.ayudacampus.data.local

import com.rosalesm.ayudacampus.data.local.entity.Categoria
import com.rosalesm.ayudacampus.data.local.entity.Comentario
import com.rosalesm.ayudacampus.data.local.entity.Ticket
import com.rosalesm.ayudacampus.data.local.entity.Usuario
import com.rosalesm.ayudacampus.model.Rol

object DatosIniciales {

    suspend fun sembrar(db: AyudaDatabase) {
        val usuarioDao = db.usuarioDao()
        val categoriaDao = db.categoriaDao()
        val ticketDao = db.ticketDao()

        // Solo se siembra si no hay usuarios registrados
        if (usuarioDao.contar() == 0) {
            // 1. Insertar Usuarios
            val usuarioCliente = Usuario(
                nombre = "prueba",
                correo = "prueba@prueba.com",
                clave = "1234",
                rol = Rol.CLIENTE.name
            )
            val usuarioJuan = Usuario(
                nombre = "Juan Pérez",
                correo = "juan@campus.test",
                clave = "1234",
                rol = Rol.CLIENTE.name
            )
            val usuarioSoporte = Usuario(
                nombre = "Soporte TI",
                correo = "soporte@campus.test",
                clave = "admin",
                rol = Rol.ADMIN.name
            )
            usuarioDao.insertarTodas(listOf(usuarioCliente, usuarioJuan, usuarioSoporte))

            // 2. Insertar Categorías
            val categorias = listOf(
                Categoria(id = 1, nombre = "Hardware", icono = "pc", activa = true),
                Categoria(id = 2, nombre = "Software", icono = "code", activa = true),
                Categoria(id = 3, nombre = "Red / Wi-Fi", icono = "wifi", activa = true),
                Categoria(id = 4, nombre = "Cuenta", icono = "key", activa = true),
                Categoria(id = 5, nombre = "Impresión", icono = "printer", activa = false)
            )
            categoriaDao.insertarTodas(categorias)

            // 3. Insertar Tickets de Ejemplo
            val ahora = System.currentTimeMillis()
            val horaMillis = 3600000L

            val t1 = Ticket(
                id = 1,
                clienteId = 1,
                categoriaId = 3,
                titulo = "Sin Wi-Fi en el laboratorio 3",
                descripcion = "Desde ayer no hay señal de Wi-Fi en todo el laboratorio. Afecta a 30 alumnos.",
                ubicacion = "Lab 3 - Pabellón B",
                prioridad = "ALTA",
                estado = "EN_PROCESO",
                fecha = ahora - (2 * horaMillis)
            )

            val t2 = Ticket(
                id = 2,
                clienteId = 1,
                categoriaId = 1,
                titulo = "Servidor del aula 305 caído",
                descripcion = "El servidor principal del aula no enciende y no se pueden realizar las prácticas.",
                ubicacion = "Aula 305",
                prioridad = "ALTA",
                estado = "ABIERTO",
                fecha = ahora - (3 * horaMillis) // Más de 1 hora para activar la alerta del panel
            )

            val t3 = Ticket(
                id = 3,
                clienteId = 2,
                categoriaId = 2,
                titulo = "Instalar Android Studio",
                descripcion = "Requiero la instalación de la versión más reciente en la PC 12.",
                ubicacion = "Lab 1",
                prioridad = "MEDIA",
                estado = "ABIERTO",
                fecha = ahora - (5 * horaMillis)
            )

            ticketDao.insertarTicket(t1)
            ticketDao.insertarTicket(t2)
            ticketDao.insertarTicket(t3)

            // 4. Insertar Comentarios de Ejemplo
            val c1 = Comentario(
                ticketId = 1,
                autorId = 1,
                autorNombre = "prueba",
                autorRol = "CLIENTE",
                texto = "Gracias, quedo atento.",
                fecha = ahora - horaMillis
            )
            val c2 = Comentario(
                ticketId = 1,
                autorId = 3,
                autorNombre = "Soporte TI",
                autorRol = "ADMIN",
                texto = "Reiniciamos el equipo. ¿Ya tienes señal?",
                fecha = ahora - (30 * 60000L)
            )
            ticketDao.insertarComentario(c1)
            ticketDao.insertarComentario(c2)
        }
    }
}