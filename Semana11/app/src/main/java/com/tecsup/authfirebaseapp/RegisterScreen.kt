package com.tecsup.authfirebaseapp

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val auth = remember { FirebaseAuth.getInstance() }

    var email by rememberSaveable { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            Text(
                text = "Crear cuenta ✨",
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

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    errorMessage = null
                },
                placeholder = { Text("Confirmar contraseña") },
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

                        password.length < 6 -> {
                            errorMessage =
                                "La contraseña debe tener al menos 6 caracteres."
                        }

                        password != confirmPassword -> {
                            errorMessage = "Las contraseñas no coinciden."
                        }

                        else -> {
                            isLoading = true
                            errorMessage = null

                            auth.createUserWithEmailAndPassword(
                                cleanEmail,
                                password
                            ).addOnCompleteListener { task ->
                                isLoading = false

                                if (task.isSuccessful) {
                                    // Firebase inicia sesión al crear la cuenta.
                                    // La cerramos para solicitar el login.
                                    auth.signOut()

                                    password = ""
                                    confirmPassword = ""

                                    onRegisterSuccess()
                                } else {
                                    errorMessage = when (task.exception) {
                                        is FirebaseAuthUserCollisionException ->
                                            "Este correo ya está registrado."

                                        is FirebaseAuthWeakPasswordException ->
                                            "La contraseña no cumple los " +
                                                    "requisitos de seguridad del proyecto."

                                        else ->
                                            "No se pudo crear la cuenta. " +
                                                    "Revisa tu conexión e inténtalo nuevamente."
                                    }
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
                    if (isLoading) "Creando..." else "Registrarse"
                )
            }

            TextButton(
                onClick = onNavigateBack,
                enabled = !isLoading
            ) {
                Text("← Volver al login")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}