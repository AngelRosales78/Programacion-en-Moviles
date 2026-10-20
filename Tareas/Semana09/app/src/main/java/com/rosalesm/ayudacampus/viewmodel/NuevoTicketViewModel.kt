package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import com.rosalesm.ayudacampus.data.prefs.PreferenciasStore
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NuevoTicketViewModel(
    private val repository: AyudaRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    private val _categoria = MutableStateFlow("Red / Wi-Fi")
    val categoria: StateFlow<String> = _categoria

    private val _titulo = MutableStateFlow("Sin Wi-Fi en el laboratorio 3")
    val titulo: StateFlow<String> = _titulo

    private val _ubicacion = MutableStateFlow("Lab 3 · Pabellón B")
    val ubicacion: StateFlow<String> = _ubicacion

    private val _prioridad = MutableStateFlow("Alta")
    val prioridad: StateFlow<String> = _prioridad

    private val _descripcion = MutableStateFlow("Desde ayer no hay señal de Wi-Fi en todo el laboratorio. Afecta a 30 alumnos durante la clase.")
    val descripcion: StateFlow<String> = _descripcion

    fun onCategoriaChange(nuevaCat: String) { _categoria.value = nuevaCat }
    fun onTituloChange(nuevoTitulo: String) { _titulo.value = nuevoTitulo }
    fun onUbicacionChange(nuevaUbicacion: String) { _ubicacion.value = nuevaUbicacion }
    fun onPrioridadChange(nuevaPrioridad: String) { _prioridad.value = nuevaPrioridad }
    fun onDescripcionChange(nuevaDesc: String) { _descripcion.value = nuevaDesc }
}