package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.prefs.PreferenciasStore
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import com.rosalesm.ayudacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UsuarioEstado(
    val nombre: String,
    val correo: String,
    val clave: String,
    val rol: Rol
)

class SesionViewModel(
    private val repository: AyudaRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    private val _sesion = MutableStateFlow<UsuarioEstado?>(null)
    val sesion: StateFlow<UsuarioEstado?> = _sesion

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _esModoOscuro = MutableStateFlow(false)
    val esModoOscuro: StateFlow<Boolean> = _esModoOscuro

    // Lista en memoria de usuarios registrados (incluyendo la cuenta de prueba)
    private val usuariosRegistrados = mutableListOf(
        UsuarioEstado("prueba", "prueba@prueba.com", "1234", Rol.CLIENTE),
        UsuarioEstado("Juan Pérez", "juan@campus.test", "1234", Rol.CLIENTE),
        UsuarioEstado("Soporte TI", "soporte@campus.test", "admin", Rol.ADMIN)
    )

    fun cambiarModoOscuro(activado: Boolean) {
        _esModoOscuro.value = activado
    }

    fun registrarUsuario(
        nombre: String,
        correo: String,
        clave: String,
        rol: Rol,
        onExito: () -> Unit
    ) {
        viewModelScope.launch {
            _cargando.value = true
            val nuevoUsuario = UsuarioEstado(nombre, correo, clave, rol)
            usuariosRegistrados.add(nuevoUsuario)
            _cargando.value = false
            onExito()
        }
    }

    fun login(
        correo: String,
        clave: String,
        rol: Rol,
        onExito: (Rol) -> Unit
    ) {
        viewModelScope.launch {
            _cargando.value = true
            val encontrado = usuariosRegistrados.find {
                it.correo.equals(correo, ignoreCase = true) && it.clave == clave && it.rol == rol
            }

            if (encontrado != null) {
                _sesion.value = encontrado
                _error.value = null
                _cargando.value = false
                onExito(encontrado.rol)
            } else {
                _cargando.value = false
                _error.value = "Credenciales incorrectas"
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}