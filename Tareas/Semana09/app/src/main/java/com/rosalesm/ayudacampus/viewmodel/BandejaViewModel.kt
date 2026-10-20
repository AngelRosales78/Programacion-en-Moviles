package com.rosalesm.ayudacampus.viewmodel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.local.relation.TicketConDetalle
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import com.rosalesm.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.*

class BandejaViewModel(private val repository: AyudaRepository) : RelojTicketsViewModel() {
    val filtroEstado=MutableStateFlow("Todos")
    val filtroPrioridad=MutableStateFlow<Prioridad?>(null)
    val filtroCategoria=MutableStateFlow<Int?>(null)
    val busqueda=MutableStateFlow("")
    val categorias=repository.observarCategorias().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    val tickets=combine(repository.observarTodosLosTickets(),filtroEstado,filtroPrioridad,filtroCategoria,busqueda) { lista,estado,prioridad,categoria,texto ->
        filtrarBandeja(lista,estado,prioridad,categoria,texto)
    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    val sla=observarSla(tickets)
    fun limpiarFiltros() { filtroEstado.value="Todos"; filtroPrioridad.value=null; filtroCategoria.value=null; busqueda.value="" }
    fun verAltasAbiertas() { limpiarFiltros(); filtroEstado.value="Abiertos"; filtroPrioridad.value=Prioridad.ALTA }
}
internal fun filtrarBandeja(lista: List<TicketConDetalle>,estado: String,prioridad: Prioridad?,categoria: Int?,busqueda: String): List<TicketConDetalle> {
    val texto=busqueda.trim().removePrefix("#")
    val valorEstado=when(estado) { "Abiertos" -> "ABIERTO"; "En proceso" -> "EN_PROCESO"; "Resueltos" -> "RESUELTO"; "Cerrados" -> "CERRADO"; else -> null }
    return lista.filter { d ->
        (valorEstado==null || d.ticket.estado==valorEstado) &&
        (prioridad==null || d.ticket.prioridad==prioridad.name) &&
        (categoria==null || d.ticket.categoriaId==categoria) &&
        (texto.isBlank() || d.ticket.id.toString().contains(texto) || d.ticket.titulo.contains(texto,true) || d.cliente.nombre.contains(texto,true))
    }
}
