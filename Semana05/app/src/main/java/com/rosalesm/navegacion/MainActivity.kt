package com.rosalesm.navegacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rosalesm.navegacion.navigation.AppNavigation
import com.rosalesm.navegacion.ui.theme.NavegacionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavegacionTheme() {
                AppNavigation()
            }
        }
    }
}