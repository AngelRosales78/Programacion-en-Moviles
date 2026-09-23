package com.rosalesm.clinicasalud.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rosalesm.clinicasalud.screens.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object DoctorDetail : Screen("doctor_detail/{doctorId}") {
        fun createRoute(doctorId: Int) = "doctor_detail/$doctorId"
    }
    object BookAppointment : Screen("book_appointment/{doctorId}") {
        fun createRoute(doctorId: Int) = "book_appointment/$doctorId"
    }
    object Confirmation : Screen("confirmation/{doctorName}/{date}/{time}") {
        fun createRoute(doctorName: String, date: String, time: String) =
            "confirmation/$doctorName/$date/$time"
    }
    object MyAppointments : Screen("my_appointments")
    object MedicalHistory : Screen("medical_history")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    onOpenDrawer: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // 1. Pantalla de Inicio
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                onOpenDrawer = onOpenDrawer
            )
        }

        // 2. Perfil del Médico
        composable(
            route = Screen.DoctorDetail.route,
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 1
            DoctorDetailScreen(navController, doctorId)
        }

        // 3. Agendar Cita
        composable(
            route = Screen.BookAppointment.route,
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 1
            BookAppointmentScreen(navController, doctorId)
        }

        // 4. Confirmación de Cita
        composable(
            route = Screen.Confirmation.route,
            arguments = listOf(
                navArgument("doctorName") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val doctorName = backStackEntry.arguments?.getString("doctorName") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val time = backStackEntry.arguments?.getString("time") ?: ""
            ConfirmationScreen(navController, doctorName, date, time)
        }

        // 5. Mis Citas
        composable(Screen.MyAppointments.route) {
            MyAppointmentsScreen(navController)
        }

        // 6. Historial Médico
        composable(Screen.MedicalHistory.route) {
            MedicalHistoryScreen(navController)
        }
    }
}