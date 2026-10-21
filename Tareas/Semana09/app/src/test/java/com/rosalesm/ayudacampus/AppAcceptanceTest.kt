package com.rosalesm.ayudacampus

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.ui.test.*
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState

@RunWith(RobolectricTestRunner::class)
@Config(sdk=[34],qualifiers="w411dp-h891dp-420dpi",application=AyudaApp::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class AppAcceptanceTest {
    @get:Rule val rule=createAndroidComposeRule<MainActivity>()
    private fun esperar(texto: String) {
        rule.waitUntil(30_000) { rule.onAllNodesWithText(texto).fetchSemanticsNodes().isNotEmpty() }
        rule.waitForIdle()
    }
    private fun click(texto: String) { rule.onNode(hasText(texto) and hasClickAction()).performSemanticsAction(SemanticsActions.OnClick) { it() }; rule.waitForIdle() }
    private fun captura(nombre: String) {
        rule.waitForIdle()
        val dir=File("../docs/capturas"); dir.mkdirs()
        // Draw the actual Android window with native graphics. PixelCopy requires
        // a hardware window redraw callback that Robolectric does not provide.
        lateinit var bitmap: Bitmap
        rule.runOnUiThread {
            val view=rule.activity.window.decorView
            bitmap=Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
            view.draw(Canvas(bitmap))
        }
        File(dir,"$nombre.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG,100,it) }
    }
    @Test fun recorridoCompleto() {
        esperar("Ya tengo una cuenta"); click("Ya tengo una cuenta")
        rule.onNodeWithText("Correo").performTextInput("juan@campus.test")
        rule.onNodeWithText("Contraseña").performTextInput("mal")
        click("Ingresar"); esperar("Correo o contraseña incorrectos")
        rule.onNodeWithText("Contraseña").performTextReplacement("1234")
        click("Ingresar"); esperar("Tickets")
        esperar("Mouse del equipo 8 no responde"); captura("01-estudiante-tickets")
        click("Nuevo"); esperar("Enviar ticket"); click("Enviar ticket")
        rule.onNodeWithText("El título debe tener al menos 5 caracteres").assertExists()
        rule.onNodeWithText("La descripción debe tener al menos 10 caracteres").assertExists()
        captura("02-estudiante-nuevo")
        rule.onNodeWithText("Hardware").performClick()
        rule.onNodeWithText("Título").performTextInput("Ticket de aceptación")
        rule.onNodeWithText("Descripción").performTextInput("El equipo no inicia correctamente")
        rule.onNodeWithText("Enviar ticket").performScrollTo().performSemanticsAction(SemanticsActions.OnClick) { it() }
        // Cambiar de pestaña durante la escritura no debe ser deshecho por su callback.
        click("Perfil"); esperar("Preferencias"); rule.waitUntil(10_000) { rule.onAllNodesWithContentDescription("7 tickets en total").fetchSemanticsNodes().isNotEmpty() }
        click("Tickets"); esperar("Ticket de aceptación")
        click("Perfil"); esperar("Preferencias"); captura("03-estudiante-perfil")
        rule.onNodeWithContentDescription("Cambiar a Soporte TI").performScrollTo().performSemanticsAction(SemanticsActions.OnClick) { it() }
        esperar("Panel de soporte"); esperar("Tiempo promedio de resolución"); esperar("24 h 0 min"); esperar("Abierto: 4"); esperar("Hardware"); captura("04-soporte-panel")
        click("Tickets"); esperar("Equipo del aula 305 no enciende"); captura("05-soporte-bandeja")
        click("Categorías"); esperar("Hardware"); esperar("3 tickets"); captura("06-soporte-categorias")
        rule.onNodeWithContentDescription("Editar Cuenta").performClick()
        click("Eliminar categoría")
        esperar("No se puede eliminar una categoría que ya tiene tickets")
        click("Cancelar"); click("Ajustes"); esperar("Recibir tickets")
        // El tercer switch es la recepción de tickets.
        rule.onAllNodes(isToggleable()).onLast().performClick()
        rule.onNodeWithContentDescription("Cambiar a Estudiante").performScrollTo().performSemanticsAction(SemanticsActions.OnClick) { it() }
        esperar("Mis tickets"); click("Nuevo"); esperar("Mesa de ayuda cerrada")
        rule.onNodeWithText("Enviar ticket").performScrollTo().assertIsNotEnabled()
        click("Perfil"); esperar("Preferencias")
        rule.onAllNodes(isToggleable()).onFirst().performSemanticsAction(SemanticsActions.OnClick) { it() }
        val activado=SemanticsMatcher.expectValue(SemanticsProperties.ToggleableState,ToggleableState.On)
        rule.waitUntil(10_000) { rule.onAllNodes(isToggleable() and activado).fetchSemanticsNodes().size==2 }
        rule.activityRule.scenario.recreate()
        esperar("Perfil"); click("Perfil"); esperar("Preferencias")
        rule.onAllNodes(isToggleable()).onFirst().assertIsOn()
        rule.onNodeWithText("Cerrar sesión").performScrollTo().performClick()
        esperar("Ingresar")
        click("¿No tienes cuenta?  Regístrate")
        esperar("Registrarme")
        rule.onNodeWithText("Nombre").performTextInput("Ana Registro")
        rule.onNodeWithText("Correo").performTextInput("ana@registro.test")
        rule.onNodeWithText("Contraseña").performTextInput("abcd1234")
        rule.onNodeWithText("Confirmar contraseña").performTextInput("abcd1234")
        val password=SemanticsMatcher.keyIsDefined(SemanticsProperties.Password)
        rule.onNodeWithText("Contraseña").assert(password)
        rule.onNodeWithContentDescription("Mostrar contraseña: Contraseña").performClick()
        rule.onNodeWithText("Contraseña").assert(password.not())
        rule.onNodeWithContentDescription("Mostrar contraseña: Confirmar contraseña").performClick()
        rule.onNodeWithText("Confirmar contraseña").assert(password.not())
        rule.onNodeWithContentDescription("Ocultar contraseña: Contraseña").performClick()
        rule.onNodeWithText("Contraseña").assert(password)
        rule.onNodeWithText("Registrarme").performScrollTo().performClick()
        esperar("Ingresar")
        rule.onNodeWithText("Correo").performTextReplacement("ana@registro.test")
        rule.onNodeWithText("Contraseña").performTextReplacement("abcd1234")
        click("Ingresar"); esperar("Mis tickets"); click("Perfil")
        esperar("Ana Registro"); rule.onNodeWithText("ana@registro.test").assertExists()
        rule.onNodeWithContentDescription("0 tickets en total").assertExists()
        rule.onNodeWithText("Cerrar sesión").performScrollTo().performClick()
        esperar("Ingresar"); click("Soporte TI")
        rule.onNodeWithText("Correo").performTextReplacement("soporte@soporte.com")
        rule.onNodeWithText("Contraseña").performTextReplacement("1234")
        click("Ingresar"); esperar("Panel de soporte"); click("Ajustes")
        esperar("soporte"); rule.onNodeWithText("soporte@soporte.com").assertExists()
        rule.onNodeWithText("Cerrar sesión").performScrollTo().performClick()
        esperar("Ingresar"); click("Estudiante")
        rule.onNodeWithText("Correo").performTextReplacement("prueba@prueba.com")
        rule.onNodeWithText("Contraseña").performTextReplacement("1234")
        click("Ingresar"); esperar("Mis tickets"); click("Perfil")
        esperar("prueba"); rule.onNodeWithText("prueba@prueba.com").assertExists()
    }
}
