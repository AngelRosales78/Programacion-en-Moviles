package com.rosalesm.ayudacampus.data.local

import androidx.room.withTransaction
import com.rosalesm.ayudacampus.data.local.entity.*

object DatosIniciales {
    suspend fun sembrar(db: AyudaDatabase) = db.withTransaction {
        if (db.usuarioDao().contar() != 0) return@withTransaction
        db.usuarioDao().insertarTodas(listOf(
            Usuario(1, "Juan Pérez", "juan@campus.test", "1234", "CLIENTE"),
            Usuario(2, "Soporte TI", "soporte@campus.test", "admin", "ADMIN")
        ))
        db.categoriaDao().insertarTodas(listOf(
            Categoria(1, "Hardware", "pc"), Categoria(2, "Software", "code"),
            Categoria(3, "Red / Wi-Fi", "wifi"), Categoria(4, "Cuenta", "key"),
            Categoria(5, "Impresión", "printer")
        ))
        val ahora = System.currentTimeMillis()
        val hora = 3_600_000L
        val ejemplos = listOf(
            Ticket(1,1,3,"Sin Wi-Fi en el laboratorio 3","Desde ayer no hay señal en el laboratorio.","Lab 3 · Pabellón B","ALTA","EN_PROCESO",ahora-2*hora),
            Ticket(2,1,1,"Equipo del aula 305 no enciende","El equipo principal no permite realizar las prácticas.","Aula 305","ALTA","ABIERTO",ahora-3*hora),
            Ticket(3,1,2,"Instalar Android Studio","Necesito Android Studio en la PC 12.","Lab 1","MEDIA","ABIERTO",ahora-5*hora),
            Ticket(4,1,4,"Recuperar acceso a la cuenta","No puedo ingresar al correo institucional.","Biblioteca","MEDIA","RESUELTO",ahora-26*hora),
            Ticket(5,1,5,"Impresora no reconoce papel","La impresora muestra un aviso sin papel.","Biblioteca","BAJA","CERRADO",ahora-48*hora,ahora-24*hora,4),
            Ticket(6,1,1,"Mouse del equipo 8 no responde","El puntero no se mueve aunque esté conectado.","Lab 2","BAJA","ABIERTO",ahora-hora)
        )
        ejemplos.forEach { db.ticketDao().insertarTicket(it) }
        db.ticketDao().insertarComentario(Comentario(ticketId=1,autorId=1,autorNombre="Juan Pérez",autorRol="CLIENTE",texto="Gracias, quedo atento.",fecha=ahora-hora))
        db.ticketDao().insertarComentario(Comentario(ticketId=1,autorId=2,autorNombre="Soporte TI",autorRol="ADMIN",texto="Reiniciamos el equipo. ¿Ya tienes señal?",fecha=ahora-1_800_000))
    }
}
