package com.tecsup.authfirebaseapp

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val auth = remember { FirebaseAuth.getInstance() }

    var email by rememberSaveable { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        val screenHeight = maxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .heightIn(min = screenHeight)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Programación Móvil - Tecsup",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))

            Image(
                painter = painterResource(R.drawable.tecsup_logo),
                contentDescription = "Logo de Tecsup",
                modifier = Modifier
                    .width(130.dp)
                    .height(52.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(100.dp))

            Text(
                text = "Iniciar Sesión 🔐",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                placeholder = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                placeholder = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )

            errorMessage?.let { message ->
                Spacer(Modifier.height(8.dp))

                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val cleanEmail = email.trim()

                    when {
                        !Patterns.EMAIL_ADDRESS
                            .matcher(cleanEmail).matches() -> {
                            errorMessage = "Ingresa un correo válido."
                        }

                        password.isEmpty() -> {
                            errorMessage = "Ingresa tu contraseña."
                        }

                        else -> {
                            isLoading = true
                            errorMessage = null

                            auth.signInWithEmailAndPassword(
                                cleanEmail,
                                password
                            ).addOnCompleteListener { task ->
                                isLoading = false

                                if (task.isSuccessful) {
                                    password = ""
                                    onLoginSuccess()
                                } else {
                                    errorMessage =
                                        "No se pudo iniciar sesión. " +
                                                "Revisa tus datos y tu conexión."
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                enabled = !isLoading
            ) {
                Text(
                    if (isLoading) "Ingresando..." else "Ingresar"
                )
            }

            TextButton(
                onClick = onNavigateToRegister,
                enabled = !isLoading
            ) {
                Text(
                    text = "¿No tienes cuenta? Regístrate aquí",
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.weight(1f))
            Spacer(Modifier.height(24.dp))

            Spacer(Modifier.height(12.dp))
        }
    }
}