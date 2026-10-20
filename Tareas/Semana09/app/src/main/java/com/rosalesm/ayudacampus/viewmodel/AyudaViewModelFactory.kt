package com.rosalesm.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rosalesm.ayudacampus.data.prefs.PreferenciasStore
import com.rosalesm.ayudacampus.data.repository.AyudaRepository

class AyudaViewModelFactory(
    private val repository: AyudaRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SesionViewModel::class.java) -> {
                SesionViewModel(repository, preferenciasStore) as T
            }
            modelClass.isAssignableFrom(MisTicketsViewModel::class.java) -> {
                MisTicketsViewModel(repository, preferenciasStore) as T
            }
            modelClass.isAssignableFrom(NuevoTicketViewModel::class.java) -> {
                NuevoTicketViewModel(repository, preferenciasStore) as T
            }
            else -> throw IllegalArgumentException("ViewModel no reconocido: ${modelClass.name}")
        }
    }
}