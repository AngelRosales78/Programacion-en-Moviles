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
    // Estados para las 4 notas de los cursos (0f a 20f)
    var notaFundamentos by remember { mutableFloatStateOf(0f) }
    var notaPoo by remember { mutableFloatStateOf(0f) }
    var notaMoviles by remember { mutableFloatStateOf(0f) }
    var notaBd by remember { mutableFloatStateOf(0f) }

    // Fondo con degradado suave
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
            // Barra superior (TopBar)
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
                // Título y subtítulo
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

                // Filas para asignación de notas mediante Sliders
                FilaCursoSlider("Fundamentos de Programación", "20%", notaFundamentos) { notaFundamentos = it }
                FilaCursoSlider("Programación Orientada a Objetos", "25%", notaPoo) { notaPoo = it }
                FilaCursoSlider("Programación en Móviles", "30%", notaMoviles) { notaMoviles = it }
                FilaCursoSlider("Base de Datos", "25%", notaBd) { notaBd = it }
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

            // Badge numérico en vivo
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

        // Control Slider para valores entre 0 y 20
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