package com.rosalesm.navegacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rosalesm.navegacion.navigation.Screen

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF8C73B8),
            Color(0xFFDDD2F3),
            Color(0xFFF6F3FC)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Portal Académico",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5E35B1)
                )

                Text(
                    text = "Accede a tu cuenta",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = ""
                    },
                    label = { Text("Correo Institucional") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF5E35B1)) },
                    textStyle = TextStyle(color = Color.Black),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF5E35B1),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF5E35B1)
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = ""
                    },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF5E35B1)) },
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = null)
                        }
                    },
                    textStyle = TextStyle(color = Color.Black),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF5E35B1),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF5E35B1)
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (email == "angel.rosales.m@tecsup.edu.pe" && password == "123456") {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        } else {
                            errorMessage = "Credenciales incorrectas"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E35B1))
                ) {
                    Text(text = "INICIAR SESIÓN", fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { /* Acción recuperación */ }) {
                    Text(text = "¿Olvidaste tu contraseña?", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}