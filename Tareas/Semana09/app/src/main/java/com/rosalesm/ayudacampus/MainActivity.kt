package com.rosalesm.ayudacampus

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.rosalesm.ayudacampus.ui.cliente.LoginScreen
import com.rosalesm.ayudacampus.ui.theme.AyudaCampusTheme
import com.rosalesm.ayudacampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as AyudaApp
        val sesionViewModel = SesionViewModel(app.repository, app.preferenciasStore)

        setContent {
            AyudaCampusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(
                        vm = sesionViewModel,
                        onLoginExitoso = { rol ->
                            Toast.makeText(
                                this,
                                "Inicio de sesión correcto como ${rol.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    }
}