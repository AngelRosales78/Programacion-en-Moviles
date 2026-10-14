package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.prefs.PreferenciasStore
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import com.rosalesm.ayudacampus.data.local.entity.Usuario
import com.rosalesm.ayudacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: AyudaRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val perfilActivo: StateFlow<String> = prefs.perfilActivo
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    fun login(correo: String, clave: String, rolSeleccionado: Rol, onSuccess: (Rol) -> Unit) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Ingresa el correo y la contraseña"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            val usuario = repo.login(correo.trim(), clave.trim())

            if (usuario == null) {
                _error.value = "Correo o contraseña incorrectos"
            } else if (usuario.rol != rolSeleccionado.name) {
                _error.value = "El usuario no corresponde al rol de ${if (rolSeleccionado == Rol.CLIENTE) "Estudiante" else "Soporte TI"}"
            } else {
                _usuarioActual.value = usuario
                prefs.guardarSesion(usuario.rol, usuario.id)
                onSuccess(rolSeleccionado)
            }
            _cargando.value = false
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}