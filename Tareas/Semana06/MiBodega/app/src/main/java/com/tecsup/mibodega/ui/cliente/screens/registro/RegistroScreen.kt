package com.tecsup.mibodega.ui.cliente.screens.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onCrearCuentaExitoso: (nombre: String, telefono: String, direccion: String, referencia: String, contrasena: String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }

    var errorNombre by remember { mutableStateOf(false) }
    var errorTelefono by remember { mutableStateOf(false) }
    var errorDireccion by remember { mutableStateOf(false) }
    var errorReferencia by remember { mutableStateOf(false) }
    var errorContrasena by remember { mutableStateOf(false) }

    var mostrarDialogoExito by remember { mutableStateOf(false) }

    // Diálogo de notificación
    if (mostrarDialogoExito) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = VerdeBodega,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "¡Cuenta creada con éxito!",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Tu cuenta ha sido registrada. Ahora puedes iniciar sesión con tu nombre de usuario y la contraseña que creaste.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogoExito = false
                        onCrearCuentaExitoso(nombre, telefono, direccion, referencia, contrasena)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeBodega)
                ) {
                    Text("Ir a Iniciar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoRegistro(onVolver = onVolver)

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Foto de perfil",
                tint = VerdeBodega,
                modifier = Modifier
                    .size(84.dp)
                    .background(GrisClaro, CircleShape)
                    .padding(4.dp)
            )
        }

        Spacer(Modifier.height(28.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorNombre = false
            },
            label = { Text("Nombre completo (Usuario)") },
            placeholder = { Text("Juan Pérez") },
            isError = errorNombre,
            supportingText = { if (errorNombre) Text("Este campo es obligatorio") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = {
                contrasena = it
                errorContrasena = false
            },
            label = { Text("Contraseña") },
            placeholder = { Text("Crea una contraseña") },
            visualTransformation = if (mostrarContrasena) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                    Icon(
                        imageVector = if (mostrarContrasena) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Ver contraseña"
                    )
                }
            },
            isError = errorContrasena,
            supportingText = { if (errorContrasena) Text("Este campo es obligatorio") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = telefono,
            onValueChange = {
                telefono = it
                errorTelefono = false
            },
            label = { Text("Teléfono") },
            placeholder = { Text("987 654 321") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = errorTelefono,
            supportingText = { if (errorTelefono) Text("Este campo es obligatorio") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = direccion,
            onValueChange = {
                direccion = it
                errorDireccion = false
            },
            label = { Text("Dirección de entrega") },
            placeholder = { Text("Av. Los Olivos 123") },
            isError = errorDireccion,
            supportingText = { if (errorDireccion) Text("Este campo es obligatorio") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = referencia,
            onValueChange = {
                referencia = it
                errorReferencia = false
            },
            label = { Text("Referencia") },
            placeholder = { Text("Frente al parque") },
            isError = errorReferencia,
            supportingText = { if (errorReferencia) Text("Este campo es obligatorio") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(24.dp))

        BotonPrimario(
            texto = "Crear cuenta",
            onClick = {
                val eNom = nombre.isBlank()
                val ePass = contrasena.isBlank()
                val eTel = telefono.isBlank()
                val eDir = direccion.isBlank()
                val eRef = referencia.isBlank()

                errorNombre = eNom
                errorContrasena = ePass
                errorTelefono = eTel
                errorDireccion = eDir
                errorReferencia = eRef

                if (!eNom && !ePass && !eTel && !eDir && !eRef) {
                    mostrarDialogoExito = true
                }
            }
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun EncabezadoRegistro(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }
        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f, fill = false)
        )
        Spacer(Modifier.size(48.dp))
    }
}