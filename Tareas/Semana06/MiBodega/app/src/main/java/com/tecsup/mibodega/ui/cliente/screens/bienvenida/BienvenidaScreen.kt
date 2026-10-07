package com.tecsup.mibodega.ui.cliente.screens.bienvenida

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.R
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.BotonSecundario
import com.tecsup.mibodega.ui.theme.AzulEnlace
import com.tecsup.mibodega.ui.theme.FondoClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

@Composable
fun BienvenidaScreen(
    usuarioValido: String = "admin",
    contrasenaValida: String = "1234",
    onRegistrarse: () -> Unit,
    onIniciarSesion: () -> Unit,
    onTerminos: () -> Unit = {}
) {
    var mostrarDialogLogin by remember { mutableStateOf(false) }
    var usuarioText by remember { mutableStateOf("") }
    var claveText by remember { mutableStateOf("") }
    var errorLogin by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(FondoClaro, MaterialTheme.colorScheme.background),
                    endY = 900f
                )
            )
            .safeDrawingPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        IlustracionBodega()

        Spacer(Modifier.height(16.dp))

        TituloMiBodega()

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Tus productos de siempre\nen la puerta de tu casa",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(1f))

        BotonPrimario(
            texto = "Registrarme",
            subtexto = "con mi teléfono",
            icono = rememberVectorPainter(Icons.Default.Phone),
            onClick = onRegistrarse
        )

        Spacer(Modifier.height(12.dp))

        BotonSecundario(
            texto = "Iniciar sesión",
            onClick = { mostrarDialogLogin = true }
        )

        Spacer(Modifier.height(20.dp))

        PieTerminos(onTerminos = onTerminos)

        Spacer(Modifier.height(24.dp))
    }

    // Modal de Login con validación flexible (admin/1234 o cuenta creada)
    if (mostrarDialogLogin) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogLogin = false
                errorLogin = false
            },
            title = {
                Text(
                    text = "Iniciar Sesión",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = usuarioText,
                        onValueChange = {
                            usuarioText = it
                            errorLogin = false
                        },
                        label = { Text("Usuario o Nombre") },
                        isError = errorLogin,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = claveText,
                        onValueChange = {
                            claveText = it
                            errorLogin = false
                        },
                        label = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = errorLogin,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorLogin) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Usuario o contraseña incorrectos",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val uInput = usuarioText.trim()
                        val cInput = claveText.trim()

                        val uValido = usuarioValido.trim()
                        val cValida = contrasenaValida.trim()

                        // 1. Acceso con credenciales admin por defecto
                        val esAdmin = uInput.equals("admin", ignoreCase = true) && cInput == "1234"

                        // 2. Acceso con la cuenta que el usuario acaba de registrar
                        val primerNombre = uValido.split(" ").firstOrNull() ?: ""
                        val coincideUsuario = uInput.equals(uValido, ignoreCase = true) ||
                                (primerNombre.isNotBlank() && uInput.equals(primerNombre, ignoreCase = true))
                        val coincideClave = (cInput == cValida)

                        val esUsuarioRegistrado = coincideUsuario && coincideClave

                        if (esAdmin || esUsuarioRegistrado) {
                            mostrarDialogLogin = false
                            errorLogin = false
                            onIniciarSesion()
                        } else {
                            errorLogin = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeBodega)
                ) {
                    Text("Ingresar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDialogLogin = false
                    errorLogin = false
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun IlustracionBodega() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ilustracion_bodega),
            contentDescription = "Ilustración de la bodega",
            modifier = Modifier.size(200.dp)
        )
    }
}

@Composable
private fun TituloMiBodega() {
    Text(
        text = buildAnnotatedString {
            append("Mi ")
            withStyle(SpanStyle(color = VerdeBodega)) { append("Bodega") }
        },
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun PieTerminos(onTerminos: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Al continuar aceptas nuestros",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Términos y Condiciones",
            style = MaterialTheme.typography.bodySmall,
            color = AzulEnlace,
            modifier = Modifier.clickable(onClick = onTerminos)
        )
    }
}