package com.openclassrooms.realestatemanager.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    // TO DELETE
    @Query("DELETE FROM property")
    fun nukeTable()

    @Upsert
    suspend fun upsertProperty(property: Property)

    @Query("SELECT * FROM property")
    fun getAllProperties(): Flow<List<Property>>

    @Query("SELECT * FROM property WHERE surface BETWEEN :min AND :max")
    fun getPropertyFilteredBySurface(min: Int, max: Int): Flow<List<Property>>

    @Query("SELECT * FROM property WHERE price BETWEEN :min AND :max")
    fun getPropertyFilteredByPrice(min: Int, max: Int): Flow<List<Property>>

    @Query("SELECT * FROM property WHERE agent LIKE :agent")
    fun getPropertyFilteredByAgent(agent: Agent): Flow<List<Property>>

    @Query("SELECT * FROM property WHERE address LIKE '%' || :address || '%'")
    fun getPropertyFilteredByAddress(address: String): Flow<List<Property>>

    @Query("SELECT * FROM property WHERE type LIKE :type")
    fun getPropertyFilteredByType(type: PropertyType): Flow<List<Property>>

    @Query("SELECT * FROM property WHERE pieceNumber BETWEEN :min AND :max")
    fun getPropertyFilteredByPiece(min: Int, max: Int): Flow<List<Property>>
}