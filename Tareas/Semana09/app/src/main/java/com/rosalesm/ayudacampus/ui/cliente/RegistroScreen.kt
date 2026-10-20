package com.rosalesm.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rosalesm.ayudacampus.model.Rol
import com.rosalesm.ayudacampus.ui.components.AyudaButton
import com.rosalesm.ayudacampus.viewmodel.SesionViewModel

@Composable
fun RegistroScreen(
    vm: SesionViewModel,
    onRegistroCompletado: () -> Unit,
    onIrALogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var confirmarClave by remember { mutableStateOf("") }

    var verClave by remember { mutableStateOf(false) }
    var verConfirmarClave by remember { mutableStateOf(false) }
    var errorLocal by remember { mutableStateOf<String?>(null) }

    val colorTema = Color(0xFF0891B2)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Crear Cuenta",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "Regístrate para reportar tus incidencias",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Campo Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        errorLocal = null
                    },
                    label = { Text("Nombre Completo") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Person, contentDescription = null, tint = colorTema)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorTema,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Correo
                OutlinedTextField(
                    value = correo,
                    onValueChange = {
                        correo = it
                        errorLocal = null
                    },
                    label = { Text("Correo Institucional") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Email, contentDescription = null, tint = colorTema)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorTema,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Contraseña
                OutlinedTextField(
                    value = clave,
                    onValueChange = {
                        clave = it
                        errorLocal = null
                    },
                    label = { Text("Contraseña") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Lock, contentDescription = null, tint = colorTema)
                    },
                    trailingIcon = {
                        IconButton(onClick = { verClave = !verClave }) {
                            Icon(
                                imageVector = if (verClave) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                contentDescription = "Mostrar contraseña",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    },
                    visualTransformation = if (verClave) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorTema,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Confirmar Contraseña
                OutlinedTextField(
                    value = confirmarClave,
                    onValueChange = {
                        confirmarClave = it
                        errorLocal = null
                    },
                    label = { Text("Confirmar Contraseña") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Lock, contentDescription = null, tint = colorTema)
                    },
                    trailingIcon = {
                        IconButton(onClick = { verConfirmarClave = !verConfirmarClave }) {
                            Icon(
                                imageVector = if (verConfirmarClave) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                contentDescription = "Mostrar contraseña",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    },
                    visualTransformation = if (verConfirmarClave) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorTema,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorLocal != null) {
                    Text(
                        text = errorLocal ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                AyudaButton(
                    text = "Registrarme",
                    onClick = {
                        when {
                            nombre.isBlank() || correo.isBlank() || clave.isBlank() -> {
                                errorLocal = "Por favor completa todos los campos"
                            }
                            clave != confirmarClave -> {
                                errorLocal = "Las contraseñas no coinciden"
                            }
                            else -> {
                                // Registrar en ViewModel y avanzar a Login
                                vm.registrarUsuario(
                                    nombre = nombre,
                                    correo = correo,
                                    clave = clave,
                                    rol = Rol.CLIENTE,
                                    onExito = onRegistroCompletado
                                )
                            }
                        }
                    },
                    color = colorTema,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("¿Ya tienes cuenta? ", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), fontSize = 14.sp)
                    Text(
                        text = "Inicia sesión",
                        color = colorTema,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onIrALogin() }
                    )
                }
            }
        }
    }
}