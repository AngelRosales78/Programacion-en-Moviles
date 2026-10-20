package com.rosalesm.ayudacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.rosalesm.ayudacampus.ui.navigation.NavAyuda
import com.rosalesm.ayudacampus.ui.theme.AyudaCampusTheme
import com.rosalesm.ayudacampus.viewmodel.AyudaViewModelFactory
import com.rosalesm.ayudacampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {

    private val factory by lazy {
        val app = application as AyudaApp
        AyudaViewModelFactory(app.repository, app.preferenciasStore)
    }

    private val sesionViewModel: SesionViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val esModoOscuro by sesionViewModel.esModoOscuro.collectAsState()

            AyudaCampusTheme(darkTheme = esModoOscuro) {
                val navController = rememberNavController()
                NavAyuda(
                    navController = navController,
                    sesionViewModel = sesionViewModel,
                    factory = factory
                )
            }
        }
    }
}