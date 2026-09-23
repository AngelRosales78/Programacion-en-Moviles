package com.rosalesm.clinicasalud.data

// Modelo para los médicos
data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: Double,
    val bio: String
)

// Modelo para las citas agendadas
data class Appointment(
    val id: Int,
    val doctorName: String,
    val date: String,
    val time: String,
    val status: String // "Confirmada" o "Completada"
)

// Datos de prueba para simular la base de datos local (SIN ViewModel / SIN IA)
object MockData {
    val specialties = listOf("Todas", "Cardiología", "Pediatría", "Dermatología")

    val doctors = listOf(
        Doctor(
            id = 1,
            name = "Dra. Ana Torres",
            specialty = "Cardiología",
            rating = 4.8,
            bio = "Especialista en arritmias e hipertensión, formación en la Clínica Mayo con más de 10 años de experiencia."
        ),
        Doctor(
            id = 2,
            name = "Dr. Luis Vega",
            specialty = "Pediatría",
            rating = 4.7,
            bio = "Atención integral para niños y adolescentes. Experto en desarrollo infantil y neonatología."
        ),
        Doctor(
            id = 3,
            name = "Dra. Rosa Díaz",
            specialty = "Dermatología",
            rating = 4.9,
            bio = "Especialista en dermatología clínica y estética, tratamiento del acné y cuidado integral de la piel."
        )
    )

    val sampleAppointments = listOf(
        Appointment(
            id = 101,
            doctorName = "Dra. Ana Torres",
            date = "Viernes 27",
            time = "10:30 am",
            status = "Confirmada"
        ),
        Appointment(
            id = 102,
            doctorName = "Dr. Luis Vega",
            date = "Miércoles 15",
            time = "3:00 pm",
            status = "Completada"
        )
    )
}