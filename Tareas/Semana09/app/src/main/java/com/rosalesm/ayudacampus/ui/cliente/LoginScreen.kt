package com.rosalesm.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rosalesm.ayudacampus.model.Rol
import com.rosalesm.ayudacampus.ui.components.AyudaButton
import com.rosalesm.ayudacampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onLoginExitoso: (Rol) -> Unit,
    onIrARegistro: () -> Unit = {}
) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var verClave by remember { mutableStateOf(false) }
    var rolSeleccionado by remember { mutableStateOf(Rol.CLIENTE) }

    val error by vm.error.collectAsState()
    val cargando by vm.cargando.collectAsState()

    val colorRol = if (rolSeleccionado == Rol.CLIENTE) Color(0xFF0891B2) else Color(0xFF7C3AED)
    val colorContenedor = if (rolSeleccionado == Rol.CLIENTE) Color(0xFFCFFAFE) else Color(0xFFEDE4FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = "Ingresa para continuar",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Selector de Rol (Estudiante / Soporte TI)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        rolSeleccionado = Rol.CLIENTE
                        correo = ""
                        clave = ""
                        vm.limpiarError()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (rolSeleccionado == Rol.CLIENTE) Color(0xFF0891B2) else Color.Transparent,
                        contentColor = if (rolSeleccionado == Rol.CLIENTE) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Estudiante", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }

                Button(
                    onClick = {
                        rolSeleccionado = Rol.ADMIN
                        correo = ""
                        clave = ""
                        vm.limpiarError()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (rolSeleccionado == Rol.ADMIN) Color(0xFF7C3AED) else Color.Transparent,
                        contentColor = if (rolSeleccionado == Rol.ADMIN) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Soporte TI", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Campo Correo
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    vm.limpiarError()
                },
                label = { Text("Correo") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        tint = colorRol
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorRol,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Contraseña con Visibilidad (Ojo)
            OutlinedTextField(
                value = clave,
                onValueChange = {
                    clave = it
                    vm.limpiarError()
                },
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = colorRol
                    )
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
                    focusedBorderColor = colorRol,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "¿Olvidaste tu contraseña?",
                color = colorRol,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 24.dp)
                    .clickable { }
            )

            if (error != null) {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            AyudaButton(
                text = if (cargando) "Ingresando..." else "Ingresar",
                onClick = {
                    vm.login(correo, clave, rolSeleccionado, onLoginExitoso)
                },
                color = colorRol,
                enabled = !cargando,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (rolSeleccionado == Rol.CLIENTE) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("¿No tienes cuenta? ", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), fontSize = 14.sp)
                    Text(
                        text = "Regístrate",
                        color = colorRol,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onIrARegistro() }
                    )
                }
            }
        }

        if (rolSeleccionado == Rol.ADMIN) {
            Card(
                colors = CardDefaults.cardColors(containerColor = colorContenedor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Modo práctica",
                        fontWeight = FontWeight.Bold,
                        color = colorRol,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                        color = Color(0xFF475569),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}