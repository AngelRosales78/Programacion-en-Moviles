package com.rosalesm.ayudacampus.ui.cliente
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.components.*
@Composable
fun SplashScreen(onComenzarClick: ()->Unit,onYaTengoCuentaClick: ()->Unit) {
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer.copy(alpha=.35f)).systemBarsPadding().padding(horizontal=22.dp,vertical=12.dp),horizontalAlignment=Alignment.CenterHorizontally) {
        Spacer(Modifier.weight(1f))
        Box(Modifier.size(108.dp).clip(RoundedCornerShape(28.dp)).background(MaterialTheme.colorScheme.primary),contentAlignment=Alignment.Center) { Icon(Icons.Outlined.ChatBubbleOutline,null,Modifier.size(63.dp),tint=MaterialTheme.colorScheme.onPrimary) }
        Spacer(Modifier.height(40.dp))
        Text("AyudaCampus",style=MaterialTheme.typography.headlineLarge)
        Text("Reporta y sigue tus incidencias",style=MaterialTheme.typography.bodyMedium,color=MaterialTheme.colorScheme.onSurfaceVariant,modifier=Modifier.padding(top=4.dp))
        Spacer(Modifier.height(86.dp))
        Row(horizontalArrangement=Arrangement.spacedBy(13.dp)) { listOf(Icons.Outlined.Computer,Icons.Outlined.Wifi,Icons.Outlined.Print).forEach { IconoCaja(it,tamano=76) } }
        Spacer(Modifier.weight(1f))
        AyudaButton("Comenzar",onComenzarClick)
        TextButton(onClick=onYaTengoCuentaClick) { Text("Ya tengo una cuenta",style=MaterialTheme.typography.labelLarge) }
    }
}
