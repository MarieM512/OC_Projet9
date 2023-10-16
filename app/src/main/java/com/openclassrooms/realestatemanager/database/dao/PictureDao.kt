package com.openclassrooms.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.database.entity.Picture
import com.openclassrooms.realestatemanager.database.utils.PictureTuple

@Dao
interface PictureDao {
    @Insert
    suspend fun insertPicture(picture: Picture)

    @Query("DELETE FROM picture WHERE propertyId = :propertyId AND uri = :uri AND title = :title")
    suspend fun deletePicture(propertyId: Int, uri: String, title: String)

    @Query("SELECT uri, title FROM picture WHERE propertyId LIKE :id")
    fun getPictureFromPropertyId(id: Int): MutableList<PictureTuple>
}