package com.rosalesm.ayudacampus
import com.rosalesm.ayudacampus.data.local.entity.Ticket
import com.rosalesm.ayudacampus.model.calcularSla
import org.junit.Assert.*
import org.junit.Test
class SlaTest {
    private fun t(p:String="ALTA",e:String="ABIERTO")=Ticket(1,1,1,"Equipo roto","No funciona","Lab",p,e,0L)
    @Test fun `limites por prioridad y vencimiento exacto`() {
        assertFalse(calcularSla(t(),4*3_600_000L-1).vencido)
        assertTrue(calcularSla(t(),4*3_600_000L).vencido)
        assertFalse(calcularSla(t("MEDIA"),24*3_600_000L-1).vencido)
        assertTrue(calcularSla(t("MEDIA"),24*3_600_000L).vencido)
        assertFalse(calcularSla(t("BAJA"),72*3_600_000L-1).vencido)
        assertTrue(calcularSla(t("BAJA"),72*3_600_000L).vencido)
    }
    @Test fun `resuelto y cerrado ya no cuentan como vencidos`() {
        assertFalse(calcularSla(t(e="RESUELTO"),99*3_600_000L).vencido)
        assertEquals("SLA finalizado · Cerrado",calcularSla(t(e="CERRADO"),99*3_600_000L).texto)
    }
}
