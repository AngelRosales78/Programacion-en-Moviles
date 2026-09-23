package com.rosalesm.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rosalesm.clinicasalud.data.Appointment
import com.rosalesm.clinicasalud.data.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(navController: NavController) {
    // Lista reactiva de citas en el estado local
    val appointments = remember {
        mutableStateListOf<Appointment>().apply {
            addAll(MockData.sampleAppointments)
        }
    }

    // Estado para controlar qué cita se va a cancelar
    var appointmentToCancel by remember { mutableStateOf<Appointment?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(appointments) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        onCancelClick = { appointmentToCancel = appointment }
                    )
                }
            }
        }

        // Diálogo de confirmación para cancelar cita
        appointmentToCancel?.let { appointment ->
            AlertDialog(
                onDismissRequest = { appointmentToCancel = null },
                title = { Text("Cancelar cita") },
                text = { Text("¿Estás seguro de que deseas cancelar la cita con ${appointment.doctorName} el ${appointment.date}?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val index = appointments.indexOfFirst { it.id == appointment.id }
                            if (index != -1) {
                                appointments[index] = appointments[index].copy(status = "Cancelada")
                            }
                            appointmentToCancel = null
                        }
                    ) {
                        Text("Confirmar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { appointmentToCancel = null }) {
                        Text("Volver")
                    }
                }
            )
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: Appointment,
    onCancelClick: () -> Unit
) {
    val barColor = when (appointment.status) {
        "Confirmada" -> Color(0xFF5E35B1)
        "Cancelada" -> Color(0xFFD32F2F)
        else -> Color.Gray
    }

    val badgeBg = when (appointment.status) {
        "Confirmada" -> Color(0xFFE8F5E9)
        "Cancelada" -> Color(0xFFFFEBEE)
        else -> Color(0xFFEEEEEE)
    }

    val badgeTextColor = when (appointment.status) {
        "Confirmada" -> Color(0xFF2E7D32)
        "Cancelada" -> Color(0xFFC62828)
        else -> Color(0xFF616161)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F3FB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(barColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = appointment.doctorName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF2C1B4D)
                    )

                    if (appointment.status == "Confirmada") {
                        TextButton(onClick = onCancelClick) {
                            Text("Cancelar", fontSize = 12.sp, color = Color.Red)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${appointment.date}, ${appointment.time}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = appointment.status,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = badgeTextColor
                    )
                }
            }
        }
    }
}