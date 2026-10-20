package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosalesm.ayudacampus.data.local.entity.Usuario
import com.rosalesm.ayudacampus.data.prefs.PreferenciasStore
import com.rosalesm.ayudacampus.data.repository.AyudaRepository
import com.rosalesm.ayudacampus.model.Rol
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SesionViewModel(private val repository: AyudaRepository, private val prefs: PreferenciasStore) : ViewModel() {
    private val _sesion = MutableStateFlow<Usuario?>(null)
    val sesion = _sesion.asStateFlow()
    val error = MutableStateFlow<String?>(null)
    val cargando = MutableStateFlow(false)
    val lista = MutableStateFlow(false)
    private var estudianteId: Int? = null
    val esModoOscuro = prefs.modoOscuro.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val notificaciones = prefs.notificaciones.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val recibirTickets = prefs.recibirTickets.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    init {
        viewModelScope.launch {
            try {
                val id = prefs.usuarioId.first()
                val rol = prefs.perfilActivo.first()
                _sesion.value = repository.obtenerUsuario(id)?.takeIf { it.rol == rol }
                if (_sesion.value?.rol == Rol.CLIENTE.name) estudianteId = id
            } catch (e: Exception) { error.value = "No se pudo restaurar la sesión" }
            finally { lista.value = true }
        }
    }
    private fun ejecutar(accion: suspend () -> Unit) {
        if (cargando.value) return
        viewModelScope.launch {
            cargando.value = true
            try { error.value = null; accion() }
            catch (e: Exception) { error.value = e.message ?: "No se pudo completar la operación" }
            finally { cargando.value = false }
        }
    }
    fun login(correo: String, clave: String, rol: Rol, onExito: (Rol) -> Unit) = ejecutar {
        require(correo.isNotBlank() && clave.isNotBlank()) { "Ingresa el correo y la contraseña" }
        val usuario = repository.login(correo.trim(), clave)
        requireNotNull(usuario) { "Correo o contraseña incorrectos" }
        require(usuario.rol == rol.name) { "El usuario no corresponde al rol seleccionado" }
        prefs.guardarSesion(usuario.rol, usuario.id)
        _sesion.value = usuario
        if (rol == Rol.CLIENTE) estudianteId = usuario.id
        onExito(rol)
    }
    fun registrarUsuario(nombre: String, correo: String, clave: String, rol: Rol, onExito: () -> Unit) = ejecutar {
        require(nombre.trim().length >= 2) { "Ingresa un nombre válido" }
        require(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(correo.trim())) { "Ingresa un correo válido" }
        require(clave.length >= 4) { "La contraseña debe tener al menos 4 caracteres" }
        require(rol == Rol.CLIENTE) { "Solo se permite registrar estudiantes" }
        repository.registrarUsuario(Usuario(nombre=nombre.trim(),correo=correo.trim().lowercase(),clave=clave,rol=rol.name))
        onExito()
    }
    fun cambiarPerfil() = ejecutar {
        val actual = _sesion.value ?: return@ejecutar
        val destino = if (actual.rol == Rol.CLIENTE.name) {
            estudianteId = actual.id
            repository.obtenerUsuarioPorRol(Rol.ADMIN.name)
        } else estudianteId?.let { repository.obtenerUsuario(it) } ?: repository.obtenerUsuarioPorRol(Rol.CLIENTE.name)
        requireNotNull(destino) { "No existe un usuario de prueba para ese perfil" }
        prefs.guardarSesion(destino.rol, destino.id)
        _sesion.value = destino
    }
    fun cerrarSesion(onCerrado: () -> Unit = {}) = ejecutar {
        prefs.cerrarSesion(); _sesion.value = null; estudianteId = null; onCerrado()
    }
    fun cambiarModoOscuro(b: Boolean) { viewModelScope.launch { prefs.guardarModoOscuro(b) } }
    fun cambiarNotificaciones(b: Boolean) { viewModelScope.launch { prefs.guardarNotificaciones(b) } }
    fun cambiarRecibirTickets(b: Boolean) { viewModelScope.launch { prefs.guardarRecibirTickets(b) } }
    fun limpiarError() { error.value = null }
}
