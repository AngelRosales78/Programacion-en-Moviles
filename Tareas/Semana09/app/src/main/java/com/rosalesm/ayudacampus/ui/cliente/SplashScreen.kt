package com.rosalesm.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rosalesm.ayudacampus.ui.components.AyudaButton

@Composable
fun SplashScreen(
    onComenzarClick: () -> Unit,
    onYaTengoCuentaClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF0FAFC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color(0xFF0891B2), shape = RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "💬", fontSize = 42.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "AyudaCampus",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                Text(
                    text = "Reporta y sigue tus incidencias",
                    fontSize = 15.sp,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconoServicioItem(icon = Icons.Outlined.Computer)
                    IconoServicioItem(icon = Icons.Outlined.Wifi)
                    IconoServicioItem(icon = Icons.Outlined.Print)
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AyudaButton(
                    text = "Comenzar",
                    onClick = onComenzarClick,
                    color = Color(0xFF0891B2),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ya tengo una cuenta",
                    color = Color(0xFF0891B2),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onYaTengoCuentaClick() }
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun IconoServicioItem(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .background(Color(0xFFCFFAFE), shape = RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF0891B2),
            modifier = Modifier.size(32.dp)
        )
    }
}