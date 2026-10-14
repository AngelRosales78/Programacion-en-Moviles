package com.rosalesm.ayudacampus.data.local.dao

import androidx.room.*
import com.rosalesm.ayudacampus.data.local.entity.Categoria
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    @Query("SELECT * FROM categorias ORDER BY nombre")
    fun observarTodas(): Flow<List<Categoria>>

    @Query("SELECT * FROM categorias WHERE activa = 1 ORDER BY nombre")
    fun observarActivas(): Flow<List<Categoria>>

    @Query("SELECT COUNT(*) FROM categorias")
    suspend fun contar(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(c: Categoria): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(lista: List<Categoria>)

    @Update
    suspend fun actualizar(c: Categoria)

    @Delete
    suspend fun eliminar(c: Categoria)

    @Query("UPDATE categorias SET activa = :activa WHERE id = :id")
    suspend fun cambiarActiva(id: Int, activa: Boolean)
}