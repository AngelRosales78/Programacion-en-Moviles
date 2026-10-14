package com.rosalesm.ayudacampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.rosalesm.ayudacampus.data.local.entity.Categoria
import com.rosalesm.ayudacampus.data.local.entity.Comentario
import com.rosalesm.ayudacampus.data.local.entity.Ticket
import com.rosalesm.ayudacampus.data.local.entity.Usuario

data class TicketConDetalle(
    @Embedded val ticket: Ticket,
    @Relation(
        parentColumn = "categoriaId",
        entityColumn = "id"
    )
    val categoria: Categoria,
    @Relation(
        parentColumn = "clienteId",
        entityColumn = "id"
    )
    val cliente: Usuario,
    @Relation(
        parentColumn = "id",
        entityColumn = "ticketId"
    )
    val comentarios: List<Comentario>
)