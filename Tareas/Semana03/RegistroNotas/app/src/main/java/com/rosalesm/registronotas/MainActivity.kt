package com.rosalesm.registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaRegistroNotas(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PantallaRegistroNotas(modifier: Modifier = Modifier) {
    var notaFundamentos by remember { mutableFloatStateOf(0f) }
    var notaPoo by remember { mutableFloatStateOf(0f) }
    var notaMoviles by remember { mutableFloatStateOf(0f) }
    var notaBd by remember { mutableFloatStateOf(0f) }

    var redondearPromedio by remember { mutableStateOf(false) }
    var confirmacionChecked by remember { mutableStateOf(false) }
    var mostrarResumen by remember { mutableStateOf(false) }

    val fondoGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFEDE7F6), Color(0xFFF3E5F5))
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(fondoGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // TopBar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF5E35B1))
                    .padding(vertical = 16.dp, horizontal = 20.dp)
            ) {
                Text(
                    text = "Registro de Notas",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Notas del ciclo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Desliza para asignar cada nota (0 a 20)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                FilaCursoSlider("Fundamentos de Programación", "20%", notaFundamentos) { notaFundamentos = it }
                FilaCursoSlider("Programación Orientada a Objetos", "25%", notaPoo) { notaPoo = it }
                FilaCursoSlider("Programación en Móviles", "30%", notaMoviles) { notaMoviles = it }
                FilaCursoSlider("Base de Datos", "25%", notaBd) { notaBd = it }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Redondear promedio final",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Switch(
                        checked = redondearPromedio,
                        onCheckedChange = { redondearPromedio = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF5E35B1),
                            checkedTrackColor = Color(0xFFD1C4E9)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = confirmacionChecked,
                        onCheckedChange = { confirmacionChecked = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF5E35B1))
                    )
                    Text(
                        text = "Confirmo que las notas son correctas",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { mostrarResumen = true },
                    enabled = confirmacionChecked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E35B1))
                ) {
                    Text(
                        text = "CALCULAR PROMEDIO",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FilaCursoSlider(
    nombreCurso: String,
    porcentaje: String,
    notaActual: Float,
    onNotaChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = nombreCurso,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "($porcentaje)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF5E35B1)
                )
            }

            Surface(
                color = Color(0xFF5E35B1).copy(alpha = 0.12f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${notaActual.toInt()}",
                    color = Color(0xFF5E35B1),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Slider(
            value = notaActual,
            onValueChange = onNotaChange,
            valueRange = 0f..20f,
            steps = 19,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF5E35B1),
                activeTrackColor = Color(0xFF5E35B1)
            )
        )
    }
}