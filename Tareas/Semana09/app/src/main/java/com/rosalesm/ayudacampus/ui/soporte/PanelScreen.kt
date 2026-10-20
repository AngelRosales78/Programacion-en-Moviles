package com.rosalesm.ayudacampus.ui.soporte
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.ui.components.graficos.*
import com.rosalesm.ayudacampus.viewmodel.PanelViewModel
@Composable
fun PanelScreen(vm: PanelViewModel,onVerTicketsPrioridadAlta: () -> Unit) {
    val promedio by vm.promedioResolucion.collectAsState()
    val abiertos by vm.abiertos.collectAsState(); val proceso by vm.enProceso.collectAsState(); val resueltos by vm.resueltos.collectAsState()
    val estados by vm.datosGraficoEstado.collectAsState(); val categorias by vm.datosGraficoCategoria.collectAsState(); val alerta by vm.altasSinAtender.collectAsState()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
        Cabecera("Panel de soporte","Mesa de ayuda TI · Vista general")
        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            listOf(Triple("Abiertos",abiertos,Color(0xFFD97706)),Triple("En proceso",proceso,Color(0xFF2563EB)),Triple("Resueltos",resueltos,Color(0xFF16A34A))).forEach { (nombre,valor,color) ->
                Card(Modifier.weight(1f),colors=CardDefaults.cardColors(containerColor=color.copy(alpha=0.12f),contentColor=color)) {
                    Column(Modifier.padding(10.dp)) { Text("$valor",style=MaterialTheme.typography.headlineMedium); Text(nombre,style=MaterialTheme.typography.labelMedium) }
                }
            }
        }
        Card { Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text("Tiempo promedio de resolución",style=MaterialTheme.typography.titleMedium)
            Text(promedio,style=MaterialTheme.typography.headlineSmall,color=MaterialTheme.colorScheme.primary)
            Text("Desde la creación hasta el cierre confirmado por el estudiante",style=MaterialTheme.typography.bodySmall)
        } }
        if(alerta>0) Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.errorContainer)) { Column(Modifier.padding(16.dp)) {
            Text("$alerta tickets ALTA sin atender por más de 1 hora")
            TextButton(onClick=onVerTicketsPrioridadAlta) { Text("Ver") }
        } }
        Card { Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) { Text("Tickets por estado",style=MaterialTheme.typography.titleMedium); GraficoDona(estados) } }
        Card { Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) { Text("Tickets por categoría",style=MaterialTheme.typography.titleMedium); GraficoBarras(categorias) } }
    }
}
