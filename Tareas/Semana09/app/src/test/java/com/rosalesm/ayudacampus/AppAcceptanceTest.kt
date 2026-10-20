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
    private fun click(texto: String) { rule.onNodeWithText(texto).performSemanticsAction(SemanticsActions.OnClick) { it() }; rule.waitForIdle() }
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
        click("Perfil"); esperar("Mi perfil"); esperar("7 tickets en total")
        click("Tickets"); esperar("Ticket de aceptación")
        click("Perfil"); esperar("Mi perfil"); captura("03-estudiante-perfil")
        rule.onNodeWithText("Cambiar a Soporte TI").performScrollTo().performSemanticsAction(SemanticsActions.OnClick) { it() }
        esperar("Panel de soporte"); esperar("Tiempo promedio de resolución"); esperar("24 h 0 min"); esperar("Abierto: 4"); esperar("Hardware"); captura("04-soporte-panel")
        click("Tickets"); esperar("Bandeja de tickets"); esperar("Equipo del aula 305 no enciende"); captura("05-soporte-bandeja")
        click("Categorías"); esperar("Nueva categoría"); esperar("Hardware"); esperar("3 tickets"); captura("06-soporte-categorias")
        rule.onAllNodesWithText("Eliminar").onFirst().performClick()
        esperar("No se puede eliminar una categoría que ya tiene tickets")
        click("Ajustes"); esperar("Recibir tickets")
        // El tercer switch es la recepción de tickets.
        rule.onAllNodes(isToggleable()).onLast().performClick()
        rule.onNodeWithText("Cambiar a Estudiante").performScrollTo().performSemanticsAction(SemanticsActions.OnClick) { it() }
        esperar("Hola, Juan 👋"); click("Nuevo"); esperar("Mesa de ayuda cerrada")
        rule.onNodeWithText("Enviar ticket").performScrollTo().assertIsNotEnabled()
        click("Perfil"); esperar("Mi perfil")
        rule.onAllNodes(isToggleable()).onFirst().performSemanticsAction(SemanticsActions.OnClick) { it() }
        val activado=SemanticsMatcher.expectValue(SemanticsProperties.ToggleableState,ToggleableState.On)
        rule.waitUntil(10_000) { rule.onAllNodes(isToggleable() and activado).fetchSemanticsNodes().size==2 }
        rule.activityRule.scenario.recreate()
        esperar("Perfil"); click("Perfil"); esperar("Mi perfil")
        rule.onAllNodes(isToggleable()).onFirst().assertIsOn()
    }
}
