package com.rosalesm.ayudacampus.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import com.rosalesm.ayudacampus.model.EstadoTicket
import com.rosalesm.ayudacampus.ui.components.graficos.DatoGrafico
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class PanelViewModel(
    private val repository: AyudaRepository
) : ViewModel() {

    val abiertos: StateFlow<Int> = repository.observarCantidadPorEstado(EstadoTicket.ABIERTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val enProceso: StateFlow<Int> = repository.observarCantidadPorEstado(EstadoTicket.EN_PROCESO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val resueltos: StateFlow<Int> = repository.observarCantidadPorEstado(EstadoTicket.RESUELTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val cerrados: StateFlow<Int> = repository.observarCantidadPorEstado(EstadoTicket.CERRADO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val promedioResolucion: StateFlow<String> = repository.observarPromedioResolucion()
        .map { millis -> millis?.let { formatearDuracion(it.toLong()) } ?: "Sin tickets cerrados" }
        .stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),"Sin tickets cerrados")

    // Datos procesados para el GraficoDona de tickets por estado
    val datosGraficoEstado: StateFlow<List<DatoGrafico>> = combine(
        abiertos, enProceso, resueltos, cerrados
    ) { ab, en, re, ce ->
        listOf(
            DatoGrafico("Abierto", ab.toFloat(), Color(0xFFD97706)),
            DatoGrafico("En proceso", en.toFloat(), Color(0xFF2563EB)),
            DatoGrafico("Resuelto", re.toFloat(), Color(0xFF16A34A)),
            DatoGrafico("Cerrado", ce.toFloat(), Color(0xFF6B7280))
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Datos procesados para el GraficoBarras por categoría
    val datosGraficoCategoria: StateFlow<List<DatoGrafico>> = repository.observarCantidadPorCategoria()
        .map { lista ->
            lista.map { item ->
                DatoGrafico(item.nombreCategoria, item.cantidad.toFloat(), Color(0xFF7C3AED))
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Alerta de tickets de prioridad ALTA sin atender por más de 1 hora
    val altasSinAtender: StateFlow<Int> = flow {
        while(true) { emit(System.currentTimeMillis()-3_600_000L); delay(60_000L) }
    }.flatMapLatest { repository.observarAltasSinAtender(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}
internal fun formatearDuracion(millis: Long): String {
    val minutos=millis.coerceAtLeast(0)/60_000L
    return when {
        millis < 60_000L -> "Menos de 1 min"
        minutos < 60 -> "$minutos min"
        else -> "${minutos/60} h ${minutos%60} min"
    }
}
