package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.openclassrooms.realestatemanager.database.entity.Property
import com.openclassrooms.realestatemanager.database.utils.Agent
import com.openclassrooms.realestatemanager.database.utils.PropertyType
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
            "INNER JOIN nearInterestPoint ON nearInterestPoint.propertyId = property.id " +
            "INNER JOIN picture ON picture.propertyId = property.id " +
            "WHERE property.surface BETWEEN :minSurface AND :maxSurface " +
            "AND property.price BETWEEN :minPrice AND :maxPrice " +
            "AND (:agent IS NULL OR property.agent LIKE :agent) " +
            "AND property.address LIKE '%' || :address || '%' " +
            "AND (:type IS NULL OR property.type LIKE :type) " +
            "AND property.pieceNumber BETWEEN :minPiece AND :maxPiece " +
            "AND (:near1 IS NULL OR nearInterestPoint.nearInterestPoint = :near1) " +
            "OR (:near2 IS '' OR nearInterestPoint.nearInterestPoint = :near2) " +
            "OR (:near3 IS '' OR nearInterestPoint.nearInterestPoint = :near3) " +
            "GROUP BY property.id HAVING COUNT(picture.propertyId) >= :picture " +

            "AND (:entryDate IS NULL OR property.entryDate BETWEEN DATE('now', :entryDate) AND DATE('now')) " +
            "AND (:soldDate IS NULL OR property.soldDate BETWEEN DATE('now', :soldDate) AND DATE('now')) ",
    )
    fun getPropertyFiltered(minSurface: Int, maxSurface: Int, minPrice: Int, maxPrice: Int, agent: Agent?, address: String, type: PropertyType?, minPiece: Int, maxPiece: Int, picture: Int, entryDate: String?, soldDate: String?, near1: String?, near2: String?, near3: String?): Flow<List<Property>>

    @Query("SELECT * FROM property WHERE id = :id")
    fun getPropertiesWithCursor(vararg id: Long): Cursor
}
