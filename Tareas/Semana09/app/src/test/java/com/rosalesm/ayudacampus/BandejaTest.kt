package com.rosalesm.ayudacampus
import com.rosalesm.ayudacampus.data.local.entity.*
import com.rosalesm.ayudacampus.data.local.relation.TicketConDetalle
import com.rosalesm.ayudacampus.model.Prioridad
import com.rosalesm.ayudacampus.viewmodel.filtrarBandeja
import org.junit.Assert.*
import org.junit.Test
class BandejaTest {
    private fun d(id:Int,p:String="ALTA",c:Int=1,e:String="ABIERTO")=TicketConDetalle(Ticket(id,1,c,"Wi-Fi no funciona","Sin señal en el laboratorio","Lab",p,e,id.toLong()),Categoria(c,"Red","wifi"),Usuario(1,"Juan Pérez","juan@campus.test","1234","CLIENTE"),emptyList())
    @Test fun `combina cuatro filtros y conserva el orden original`() {
        val lista=listOf(d(9),d(7),d(8,c=2),d(6,e="RESUELTO"),d(5,p="MEDIA"))
        assertEquals(listOf(9,7),filtrarBandeja(lista,"Abiertos",Prioridad.ALTA,1,"wi-fi").map { it.ticket.id })
        assertEquals(listOf(7),filtrarBandeja(lista,"Abiertos",Prioridad.ALTA,1," #7 ").map { it.ticket.id })
        assertEquals(5,filtrarBandeja(lista,"Todos",null,null,"JUAN").size)
        assertTrue(filtrarBandeja(lista,"Resueltos",Prioridad.BAJA,null,"").isEmpty())
    }
}
