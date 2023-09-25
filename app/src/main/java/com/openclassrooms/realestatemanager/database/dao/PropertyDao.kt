package com.openclassrooms.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.openclassrooms.realestatemanager.database.entity.Property
import com.openclassrooms.realestatemanager.database.utils.Agent
import com.openclassrooms.realestatemanager.database.utils.PropertyType
import com.openclassrooms.realestatemanager.database.utils.Status
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Upsert
    suspend fun upsertProperty(property: Property): Long

    @Query("SELECT * FROM property")
    fun getAllProperties(): Flow<List<Property>>

    @Query(
        "SELECT property.id, type, price, surface, pieceNumber, description, address, latitude, longitude, status, entryDate, soldDate, agent " +
            "FROM property " +
            "LEFT JOIN nearInterestPoint ON nearInterestPoint.propertyId = property.id " +
            "INNER JOIN picture ON picture.propertyId = property.id " +
            "WHERE property.surface BETWEEN :minSurface AND :maxSurface " +
            "AND property.price BETWEEN :minPrice AND :maxPrice " +
            "AND (:agent IS NULL OR property.agent LIKE :agent) " +
            "AND property.address LIKE '%' || :address || '%' " +
            "AND (:type IS NULL OR property.type LIKE :type) " +
            "AND property.pieceNumber BETWEEN :minPiece AND :maxPiece " +
            "GROUP BY property.id HAVING COUNT(picture.propertyId) >= :picture " +
            "AND (:entryDate IS NULL OR property.entryDate BETWEEN DATE('now', :entryDate) AND DATE('now')) " +
            "AND (:soldDate IS NULL OR property.soldDate BETWEEN DATE('now', :soldDate) AND DATE('now'))",
    )
    fun getPropertyFiltered(minSurface: Int, maxSurface: Int, minPrice: Int, maxPrice: Int, agent: Agent?, address: String, type: PropertyType?, minPiece: Int, maxPiece: Int, picture: Int, entryDate: String?, soldDate: String?): Flow<List<Property>>
}
