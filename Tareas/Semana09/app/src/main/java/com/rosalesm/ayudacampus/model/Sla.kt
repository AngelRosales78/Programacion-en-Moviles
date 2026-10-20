package com.rosalesm.ayudacampus.model

import com.rosalesm.ayudacampus.data.local.entity.Ticket

data class EstadoSla(val texto: String,val vencido: Boolean)
fun calcularSla(ticket: Ticket,ahora: Long): EstadoSla {
    if(ticket.estado=="CERRADO") return EstadoSla("SLA finalizado · Cerrado",false)
    if(ticket.estado=="RESUELTO") return EstadoSla("SLA atendido · Resuelto",false)
    val horas=when(ticket.prioridad) { "ALTA" -> 4; "MEDIA" -> 24; else -> 72 }
    val restante=horas*3_600_000L - (ahora-ticket.fecha).coerceAtLeast(0L)
    val minutos=(kotlin.math.abs(restante)+59_999)/60_000
    val duracion=if(minutos<60) "$minutos min" else "${minutos/60} h ${minutos%60} min"
    return EstadoSla(if(restante<=0) "SLA vencido · $duracion" else "SLA restante · $duracion",restante<=0)
}
