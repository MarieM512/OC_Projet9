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

    @Query(
        "SELECT * FROM property WHERE (:minSurface AND :maxSurface IS NULL OR surface BETWEEN :minSurface AND :maxSurface) " +
            "AND (:minPrice AND :maxPrice IS NULL OR price BETWEEN :minPrice AND :maxPrice) " +
            "AND (:agent IS NULL OR agent LIKE :agent) " +
            "AND (:address IS NULL OR address LIKE '%' || :address || '%') " +
            "AND (:type IS NULL OR type LIKE :type) " +
            "AND (:minPiece AND :maxPiece IS NULL OR pieceNumber BETWEEN :minPiece AND :maxPiece)",
    )
    fun getPropertyFiltered(minSurface: Int, maxSurface: Int, minPrice: Int, maxPrice: Int, agent: Agent, address: String, type: PropertyType, minPiece: Int, maxPiece: Int): Flow<List<Property>>
}
