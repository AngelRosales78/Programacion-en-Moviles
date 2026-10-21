package com.rosalesm.ayudacampus.ui.components
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rosalesm.ayudacampus.data.local.entity.Usuario
@Composable
fun Tarjeta(modifier: Modifier=Modifier,content: @Composable ColumnScope.()->Unit) {
    Surface(modifier.fillMaxWidth(),shape=RoundedCornerShape(14.dp),color=MaterialTheme.colorScheme.surface,border=BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant)) {
        Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(8.dp),content=content)
    }
}
@Composable
fun IconoCaja(icono: ImageVector,modifier: Modifier=Modifier,tamano: Int=38) {
    Box(modifier.size(tamano.dp).clip(RoundedCornerShape(11.dp)).background(MaterialTheme.colorScheme.primaryContainer),contentAlignment=Alignment.Center) {
        Icon(icono,null,Modifier.size(21.dp),tint=MaterialTheme.colorScheme.primary)
    }
}
@Composable
fun Identidad(usuario: Usuario,soporte: Boolean=false) {
    Tarjeta {
        Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(54.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),contentAlignment=Alignment.Center) { Icon(Icons.Outlined.Person,null,Modifier.size(30.dp),tint=MaterialTheme.colorScheme.primary) }
            Column(verticalArrangement=Arrangement.spacedBy(2.dp)) {
                Text(usuario.nombre,style=MaterialTheme.typography.titleMedium)
                Text(usuario.correo,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
                Surface(color=MaterialTheme.colorScheme.primaryContainer,shape=RoundedCornerShape(6.dp)) { Text(if(soporte) "Perfil activo" else "Estudiante",Modifier.padding(horizontal=7.dp,vertical=2.dp),style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.primary) }
            }
        }
    }
}
@Composable
fun Contador(titulo: String,valor: Int,color: Color,modifier: Modifier=Modifier,descripcion: String="$valor $titulo") {
    Column(modifier.semantics { contentDescription=descripcion }.clip(RoundedCornerShape(14.dp)).background(color.copy(alpha=.13f)).padding(vertical=13.dp,horizontal=8.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(4.dp)) {
        Text("$valor",style=MaterialTheme.typography.headlineMedium,color=color)
        Text(titulo,style=MaterialTheme.typography.labelSmall,color=color)
    }
}
@Composable
fun CambiarPerfil(destino: String,onClick: ()->Unit) {
    Surface(Modifier.fillMaxWidth().semantics { contentDescription="Cambiar a $destino" }.clip(RoundedCornerShape(13.dp)).clickable(onClick=onClick),color=MaterialTheme.colorScheme.primary,contentColor=MaterialTheme.colorScheme.onPrimary) {
        Row(Modifier.padding(14.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Outlined.SwapHoriz,null,Modifier.size(24.dp))
            Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(2.dp)) { Text("Cambiar de perfil",fontWeight=FontWeight.Bold); Text("Modo práctica: ir a vista $destino",style=MaterialTheme.typography.labelSmall) }
            Icon(Icons.AutoMirrored.Outlined.ArrowForward,null,Modifier.size(18.dp))
        }
    }
}
@Composable
fun Preferencia(titulo: String,detalle: String,valor: Boolean,onCambio: (Boolean)->Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min=54.dp),verticalAlignment=Alignment.CenterVertically) {
        Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(3.dp)) { Text(titulo,style=MaterialTheme.typography.titleSmall); Text(detalle,style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant) }
        Switch(checked=valor,onCheckedChange=onCambio,modifier=Modifier.height(32.dp))
    }
}
@Composable
fun AvisoPreferencias() {
    Surface(Modifier.fillMaxWidth(),color=MaterialTheme.colorScheme.primaryContainer.copy(alpha=.35f),shape=RoundedCornerShape(10.dp)) {
        Column(Modifier.padding(13.dp)) { Text("Se guardan en el dispositivo",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant); Text("con DataStore",style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.primary) }
    }
}
@Composable
fun BotonSalir(onClick: ()->Unit) {
    OutlinedButton(onClick=onClick,modifier=Modifier.fillMaxWidth().height(46.dp),shape=RoundedCornerShape(11.dp),border=BorderStroke(1.dp,MaterialTheme.colorScheme.error),colors=ButtonDefaults.outlinedButtonColors(contentColor=MaterialTheme.colorScheme.error)) { Text("Cerrar sesión") }
}
@Composable
fun Pill(texto: String,seleccionado: Boolean,onClick: ()->Unit,modifier: Modifier=Modifier,color: Color=MaterialTheme.colorScheme.primary,enabled: Boolean=true,icono: ImageVector?=null) {
    FilterChip(selected=seleccionado,onClick=onClick,enabled=enabled,label={ Text(texto,style=MaterialTheme.typography.labelSmall) },leadingIcon=if(icono!=null) ({ Icon(icono,null,Modifier.size(16.dp)) }) else null,
        shape=RoundedCornerShape(18.dp),colors=FilterChipDefaults.filterChipColors(containerColor=MaterialTheme.colorScheme.surface,selectedContainerColor=color,selectedLabelColor=Color.White,selectedLeadingIconColor=Color.White),modifier=modifier.height(34.dp))
}
@Composable
fun BarraInferior(nombres: List<String>,iconos: List<ImageVector>,seleccion: Int,onSeleccion: (Int)->Unit) {
    Surface(color=MaterialTheme.colorScheme.surface,border=BorderStroke(.5.dp,MaterialTheme.colorScheme.outlineVariant)) {
        Row(Modifier.fillMaxWidth().navigationBarsPadding().height(62.dp)) {
            nombres.forEachIndexed { i,nombre ->
                val activo=i==seleccion
                Column(Modifier.weight(1f).fillMaxHeight().clickable { onSeleccion(i) }.padding(top=9.dp,bottom=5.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(4.dp)) {
                    Icon(iconos[i],null,Modifier.size(22.dp),tint=if(activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(nombre,style=MaterialTheme.typography.labelSmall,color=if(activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
