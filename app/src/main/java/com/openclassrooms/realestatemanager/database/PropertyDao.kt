package com.openclassrooms.realestatemanager.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Upsert
    suspend fun upsertProperty(property: Property)

    @Query("SELECT * FROM property")
    fun getAllProperties(): Flow<List<Property>>

    @Query(
        "SELECT * FROM property WHERE surface BETWEEN :minSurface AND :maxSurface " +
            "AND price BETWEEN :minPrice AND :maxPrice " +
            "AND (:agent IS NULL OR agent LIKE :agent) " +
            "AND address LIKE '%' || :address || '%' " +
            "AND (:type IS NULL OR type LIKE :type) " +
            "AND pieceNumber BETWEEN :minPiece AND :maxPiece",
    )
    fun getPropertyFiltered(minSurface: Int, maxSurface: Int, minPrice: Int, maxPrice: Int, agent: Agent?, address: String, type: PropertyType?, minPiece: Int, maxPiece: Int): Flow<List<Property>>
}
