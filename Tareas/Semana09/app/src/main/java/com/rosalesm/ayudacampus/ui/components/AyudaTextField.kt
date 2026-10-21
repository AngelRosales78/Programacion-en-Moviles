package com.rosalesm.ayudacampus.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
@Composable
fun AyudaTextField(value: String,onValueChange: (String)->Unit,label: String,modifier: Modifier=Modifier,isPassword: Boolean=false,isError: Boolean=false,errorMessage: String?=null,singleLine: Boolean=true,icono: ImageVector?=null) {
    var visible by rememberSaveable(label) { mutableStateOf(false) }
    Column(modifier) {
        OutlinedTextField(value=value,onValueChange=onValueChange,label={ Text(label,style=MaterialTheme.typography.bodySmall) },
            singleLine=singleLine,minLines=if(singleLine) 1 else 3,maxLines=if(singleLine) 1 else 8,isError=isError,
            textStyle=MaterialTheme.typography.bodyLarge,
            visualTransformation=if(isPassword && !visible) PasswordVisualTransformation() else VisualTransformation.None,
            leadingIcon=if(icono!=null) ({ Icon(icono,null,Modifier.size(19.dp),tint=MaterialTheme.colorScheme.primary) }) else null,
            trailingIcon=if(isPassword) ({ IconButton(onClick={ visible=!visible }) { Icon(if(visible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,if(visible) "Ocultar contraseña: $label" else "Mostrar contraseña: $label",Modifier.size(20.dp),tint=MaterialTheme.colorScheme.onSurfaceVariant) } }) else null,
            shape=RoundedCornerShape(12.dp),colors=OutlinedTextFieldDefaults.colors(
                focusedContainerColor=MaterialTheme.colorScheme.surface,unfocusedContainerColor=MaterialTheme.colorScheme.surface,
                focusedBorderColor=MaterialTheme.colorScheme.primary,unfocusedBorderColor=MaterialTheme.colorScheme.outline),
            modifier=Modifier.fillMaxWidth())
        if(isError && !errorMessage.isNullOrBlank()) Text(errorMessage,color=MaterialTheme.colorScheme.error,style=MaterialTheme.typography.bodySmall,modifier=Modifier.padding(start=4.dp,top=3.dp))
    }
}
