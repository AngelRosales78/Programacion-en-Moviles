package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.local.entity.Usuario
import com.rosalesm.ayudacampus.data.prefs.PreferenciasStore
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import com.rosalesm.ayudacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repository: AyudaRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    private val _sesion = MutableStateFlow<Usuario?>(null)
    val sesion: StateFlow<Usuario?> = _sesion

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    // Se observa el modo oscuro guardado en DataStore
    val esModoOscuro: StateFlow<Boolean> = preferenciasStore.modoOscuro.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun cambiarModoOscuro(activado: Boolean) {
        viewModelScope.launch {
            preferenciasStore.guardarModoOscuro(activado)
        }
    }

    fun login(
        correo: String,
        clave: String,
        rol: Rol,
        onExito: (Rol) -> Unit
    ) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Ingresa el correo y la contraseña"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            val usuario = repository.login(correo, clave)

            if (usuario != null) {
                if (usuario.rol != rol.name) {
                    _error.value = "El usuario no corresponde al rol seleccionado"
                } else {
                    _sesion.value = usuario
                    _error.value = null
                    preferenciasStore.guardarSesion(usuario.rol, usuario.id)
                    onExito(rol)
                }
            } else {
                _error.value = "Credenciales incorrectas"
            }
            _cargando.value = false
        }
    }

    fun cerrarSesion(onCerrado: () -> Unit) {
        viewModelScope.launch {
            preferenciasStore.cerrarSesion()
            _sesion.value = null
            onCerrado()
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}