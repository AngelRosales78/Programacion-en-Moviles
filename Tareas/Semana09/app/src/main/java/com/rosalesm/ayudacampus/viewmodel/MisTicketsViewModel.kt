package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import com.rosalesm.ayudacampus.data.prefs.PreferenciasStore
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ComentarioItem(
    val autor: String,
    val texto: String,
    val hora: String,
    val esMio: Boolean
)

data class TicketItem(
    val id: String,
    val numero: String,
    val titulo: String,
    val ubicacion: String,
    val estado: String,
    val prioridad: String,
    val tiempo: String,
    val icono: String,
    val conversacion: List<ComentarioItem> = emptyList()
)

class MisTicketsViewModel(
    private val repository: AyudaRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda

    private val _filtroSeleccionado = MutableStateFlow("Todos")
    val filtroSeleccionado: StateFlow<String> = _filtroSeleccionado

    val tickets = MutableStateFlow(
        listOf(
            TicketItem(
                id = "1",
                numero = "#0012",
                titulo = "Sin Wi-Fi en el laboratorio 3",
                ubicacion = "Lab 3",
                estado = "En proceso",
                prioridad = "Alta",
                tiempo = "Hace 2 h",
                icono = "wifi",
                conversacion = listOf(
                    ComentarioItem("Soporte TI", "Revisaremos el access point del pabellón B hoy.", "10:20", false),
                    ComentarioItem("Tú", "Gracias, quedo atento.", "10:32", true),
                    ComentarioItem("Soporte TI", "Reiniciamos el equipo. ¿Ya tienes señal?", "11:05", false)
                )
            ),
            TicketItem(
                id = "2",
                numero = "#0011",
                titulo = "Teclado no responde",
                ubicacion = "Aula 204",
                estado = "Abierto",
                prioridad = "Media",
                tiempo = "Ayer",
                icono = "computer"
            ),
            TicketItem(
                id = "3",
                numero = "#0009",
                titulo = "No puedo imprimir en biblioteca",
                ubicacion = "Biblioteca",
                estado = "Resuelto",
                prioridad = "Baja",
                tiempo = "Hace 3 d",
                icono = "print",
                conversacion = listOf(
                    ComentarioItem("Soporte TI", "Reinstalamos el driver de la impresora. Ya debería imprimir.", "Ayer", false)
                )
            )
        )
    )

    fun agregarTicket(titulo: String, ubicacion: String, prioridad: String, categoria: String) {
        val nuevoId = (tickets.value.size + 1).toString()
        val numeroGenerado = "#001${tickets.value.size + 3}"
        val icono = when (categoria) {
            "Red / Wi-Fi" -> "wifi"
            "Hardware" -> "computer"
            else -> "print"
        }

        val nuevo = TicketItem(
            id = nuevoId,
            numero = numeroGenerado,
            titulo = titulo,
            ubicacion = ubicacion,
            estado = "Abierto",
            prioridad = prioridad,
            tiempo = "Hace un momento",
            icono = icono
        )

        tickets.value = listOf(nuevo) + tickets.value
    }

    fun agregarComentario(ticketId: String, texto: String) {
        tickets.value = tickets.value.map { ticket ->
            if (ticket.id == ticketId) {
                val nuevaLista = ticket.conversacion + ComentarioItem("Tú", texto, "Ahora", true)
                ticket.copy(conversacion = nuevaLista)
            } else ticket
        }
    }

    fun onBusquedaChange(nuevoTexto: String) { _busqueda.value = nuevoTexto }
    fun onFiltroChange(nuevoFiltro: String) { _filtroSeleccionado.value = nuevoFiltro }
}