package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rosalesm.ayudacampus.data.local.entity.Usuario
import com.rosalesm.ayudacampus.data.prefs.PreferenciasStore
import com.rosalesm.ayudacampus.data.repository.AyudaRepository

class AyudaViewModelFactory(
    private val repository: AyudaRepository,
    private val preferenciasStore: PreferenciasStore,
    private val usuario: Usuario? = null,
    private val ticketId: Int = 0
) : ViewModelProvider.Factory {
    fun paraUsuario(u: Usuario, id: Int = 0) = AyudaViewModelFactory(repository, preferenciasStore, u, id)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        SesionViewModel::class.java -> SesionViewModel(repository, preferenciasStore)
        CategoriasViewModel::class.java -> CategoriasViewModel(repository)
        MisTicketsViewModel::class.java -> MisTicketsViewModel(repository, requireNotNull(usuario).id)
        NuevoTicketViewModel::class.java -> NuevoTicketViewModel(repository, requireNotNull(usuario).id, preferenciasStore)
        TicketDetalleViewModel::class.java -> TicketDetalleViewModel(repository, ticketId, requireNotNull(usuario))
        AtenderViewModel::class.java -> AtenderViewModel(repository, ticketId, requireNotNull(usuario))
        PanelViewModel::class.java -> PanelViewModel(repository)
        BandejaViewModel::class.java -> BandejaViewModel(repository)
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    } as T
}
