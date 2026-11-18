package com.tecsup.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.campusgps.data.LugaresRepository
import com.tecsup.campusgps.data.UbicacionNoDisponibleException
import com.tecsup.campusgps.data.UbicacionRepository
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.Posicion
import com.tecsup.campusgps.model.TipoError
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    // Mejora IA 1: filtro por categoría (null = todas)
    val categorias: List<String> = lugaresRepo.obtenerLugares().map { it.categoria }.distinct()

    private val _categoria = MutableStateFlow<String?>(null)
    val categoria: StateFlow<String?> = _categoria.asStateFlow()

    // Lo que dibujan el mapa y la lista: los lugares de la categoría elegida
    val lugaresVisibles: StateFlow<List<LugarConDistancia>> =
        combine(_lugares, _categoria) { lista, cat ->
            if (cat == null) lista else lista.filter { it.lugar.categoria == cat }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _lugares.value)

    // Mejora IA 2: radio de cercanía en metros
    val radios: List<Int> = listOf(100, 200, 500)

    private val _radio = MutableStateFlow(200)
    val radio: StateFlow<Int> = _radio.asStateFlow()

    // Ids de los lugares cuya distancia ya calculada cae dentro del radio
    val idsDentroDelRadio: StateFlow<Set<Int>> =
        combine(_lugares, _radio) { lista, r ->
            lista.filter { it.metros != null && it.metros <= r }
                .map { it.lugar.id }
                .toSet()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    // Paso 7: el primero con distancia calculada (la lista ya está ordenada)
    val masCercano: StateFlow<Lugar?> = lugaresVisibles
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun buscar() {
        // Paso 8: sin Play Services no tiene sentido buscar
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                "Este dispositivo no tiene Google Play Services",
                TipoError.SIN_PLAY_SERVICES
            )
            return
        }
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)
                }
                .onFailure { _estado.value = errorDe(it) }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )
    }

    // Paso 6
    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    fun cambiarRadio(metros: Int) {
        _radio.value = metros
    }

    fun filtrarCategoria(cat: String?) {
        _categoria.value = cat
        // Si el lugar seleccionado queda oculto, se quita la selección
        val sel = _seleccionado.value
        if (cat != null && sel != null &&
            _lugares.value.none { it.lugar.id == sel && it.lugar.categoria == cat }
        ) {
            _seleccionado.value = null
        }
    }

    // Paso 5: calcula, ordena de menor a mayor y publica
    private fun actualizarDistancias(pos: Posicion) {
        _lugares.value = lugaresRepo.obtenerLugares()
            .map { LugarConDistancia(it, distanciaMetros(pos, it)) }
            .sortedBy { it.metros }
    }

    private fun errorDe(e: Throwable): UbicacionUi.Error = when (e) {
        is SecurityException -> UbicacionUi.Error(
            "Falta el permiso de ubicación", TipoError.SIN_PERMISO
        )
        is UbicacionNoDisponibleException -> UbicacionUi.Error(
            "No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo",
            TipoError.GPS_APAGADO
        )
        else -> UbicacionUi.Error(
            e.message ?: "No se pudo obtener la ubicación", TipoError.OTRO
        )
    }
}
