package com.rosalesm.ayudacampus
import com.rosalesm.ayudacampus.viewmodel.formatearDuracion
import org.junit.Assert.*
import org.junit.Test
class DuracionTest {
    @Test fun `promedio formatea minutos horas y menos de un minuto`() {
        assertEquals("Menos de 1 min",formatearDuracion(0))
        assertEquals("5 min",formatearDuracion(300_000L))
        assertEquals("24 h 30 min",formatearDuracion(88_200_000L))
    }
}
