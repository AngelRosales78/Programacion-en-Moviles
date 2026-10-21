package com.rosalesm.ayudacampus.data.local.dao

import androidx.room.*
import com.rosalesm.ayudacampus.data.local.entity.Usuario

@Dao
interface UsuarioDao {
    @Update
    suspend fun actualizar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtener(id: Int): Usuario?
    @Query("SELECT * FROM usuarios WHERE rol = :rol ORDER BY CASE WHEN correo IN ('prueba@prueba.com','soporte@soporte.com') THEN 0 ELSE 1 END, id LIMIT 1")
    suspend fun obtenerPorRol(rol: String): Usuario?
    @Query("SELECT * FROM usuarios WHERE LOWER(correo) = LOWER(:correo) LIMIT 1")
    suspend fun buscarCorreo(correo: String): Usuario?
    @Insert
    suspend fun insertar(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios WHERE LOWER(correo) = LOWER(:correo) AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(lista: List<Usuario>)

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int
}