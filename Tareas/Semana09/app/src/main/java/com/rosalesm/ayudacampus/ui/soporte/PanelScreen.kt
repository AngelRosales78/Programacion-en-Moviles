package com.rosalesm.ayudacampus.ui.soporte
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.ui.components.*
import com.rosalesm.ayudacampus.ui.components.graficos.*
import com.rosalesm.ayudacampus.viewmodel.PanelViewModel
@Composable
fun PanelScreen(vm: PanelViewModel,onVerTicketsPrioridadAlta: ()->Unit) {
    val promedio by vm.promedioResolucion.collectAsState(); val abiertos by vm.abiertos.collectAsState(); val proceso by vm.enProceso.collectAsState(); val resueltos by vm.resueltos.collectAsState()
    val estados by vm.datosGraficoEstado.collectAsState(); val categorias by vm.datosGraficoCategoria.collectAsState(); val alerta by vm.altasSinAtender.collectAsState()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Cabecera("Panel de soporte","Mesa de ayuda TI · Hoy")
        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) { listOf(Triple("Abiertos",abiertos,Color(0xFFD97706)),Triple("En proceso",proceso,Color(0xFF2563EB)),Triple("Resueltos",resueltos,Color(0xFF16A34A))).forEach { (n,v,c)->Contador(n,v,c,Modifier.weight(1f)) } }
        Text("Tickets por estado",style=MaterialTheme.typography.titleMedium)
        Tarjeta { GraficoDona(estados) }
        Text("Tickets por categoría",style=MaterialTheme.typography.titleMedium)
        Tarjeta { GraficoBarras(categorias) }
        Surface(Modifier.fillMaxWidth(),shape=RoundedCornerShape(12.dp),color=MaterialTheme.colorScheme.primaryContainer.copy(alpha=.45f)) {
            Row(Modifier.padding(13.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                Text("Tiempo promedio de resolución",style=MaterialTheme.typography.bodySmall,modifier=Modifier.weight(1f)); Text(promedio,style=MaterialTheme.typography.titleMedium,color=MaterialTheme.colorScheme.primary)
            }
        }
        if(alerta>0) Surface(Modifier.fillMaxWidth(),shape=RoundedCornerShape(12.dp),color=MaterialTheme.colorScheme.errorContainer) {
            Row(Modifier.padding(12.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.WarningAmber,null,Modifier.size(20.dp),tint=MaterialTheme.colorScheme.error)
                Column(Modifier.weight(1f)) { Text("$alerta ${if(alerta==1) "ticket" else "tickets"} de prioridad Alta",style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.error); Text("sin atender hace más de 1 hora",style=MaterialTheme.typography.labelSmall) }
                Button(onClick=onVerTicketsPrioridadAlta,contentPadding=PaddingValues(horizontal=15.dp,vertical=0.dp),modifier=Modifier.height(32.dp),colors=ButtonDefaults.buttonColors(containerColor=MaterialTheme.colorScheme.error)) { Text("Ver",style=MaterialTheme.typography.labelSmall) }
            }
        }
        Spacer(Modifier.height(10.dp))
    }
}
