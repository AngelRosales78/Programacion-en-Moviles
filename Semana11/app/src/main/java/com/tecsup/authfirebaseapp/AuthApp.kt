package com.tecsup.authfirebaseapp

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

private object Destinations {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun AuthApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val startDestination = remember {
        if (FirebaseAuth.getInstance().currentUser != null) {
            Destinations.HOME
        } else {
            Destinations.LOGIN
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            composable(Destinations.LOGIN) {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate(Destinations.REGISTER) {
                            launchSingleTop = true
                        }
                    },
                    onLoginSuccess = {
                        navController.navigate(Destinations.HOME) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Destinations.REGISTER) {
                RegisterScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        Toast.makeText(
                            context,
                            "Cuenta creada. Inicia sesión con tus credenciales.",
                            Toast.LENGTH_LONG
                        ).show()

                        // Regresar a un login nuevo después del registro.
                        navController.navigate(Destinations.LOGIN) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Destinations.HOME) {
                HomeScreen(
                    onLogout = {
                        FirebaseAuth.getInstance().signOut()

                        navController.navigate(Destinations.LOGIN) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}