package com.openclassrooms.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.database.entity.NearInterestPoint

@Dao
interface NearInterestPointDao {

    @Insert
    suspend fun insertNearInterestPoint(nearInterestPoint: NearInterestPoint)

    @Query("DELETE FROM nearInterestPoint WHERE propertyId = :propertyId AND nearInterestPoint = :near")
    suspend fun deleteNearInterestPoint(propertyId: Int, near: String)

    @Query("SELECT nearInterestPoint FROM nearInterestPoint WHERE propertyId LIKE :id")
    fun getNearInterestPointFromPropertyId(id: Int): MutableList<String>
}
