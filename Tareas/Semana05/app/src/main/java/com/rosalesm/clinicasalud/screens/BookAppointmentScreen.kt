package com.rosalesm.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rosalesm.clinicasalud.data.MockData
import com.rosalesm.clinicasalud.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(
    navController: NavController,
    doctorId: Int
) {
    val doctor = MockData.doctors.find { it.id == doctorId } ?: MockData.doctors.first()

    // Opciones de fecha y hora disponibles (Mínimo 3 opciones de cada una)
    val dates = listOf("Jue 26", "Vie 27", "Sáb 28")
    val times = listOf("9:00 am", "10:30 am", "3:00 pm")

    // Estados para la selección única
    var selectedDate by remember { mutableStateOf(dates[1]) }
    var selectedTime by remember { mutableStateOf(times[1]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Selecciona fecha",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C1B4D)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Selección única de fecha
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    dates.forEach { date ->
                        val isSelected = selectedDate == date
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedDate = date },
                            label = {
                                Text(
                                    text = date,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF5E35B1),
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Selecciona hora",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C1B4D)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Selección única de hora
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    times.forEach { time ->
                        val isSelected = selectedTime == time
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTime = time },
                            label = { Text(text = time) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF5E35B1),
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Botón de Confirmación
            Button(
                onClick = {
                    navController.navigate(
                        Screen.Confirmation.createRoute(
                            doctorName = doctor.name,
                            date = selectedDate,
                            time = selectedTime
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E35B1))
            ) {
                Text(
                    text = "Confirmar cita",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}