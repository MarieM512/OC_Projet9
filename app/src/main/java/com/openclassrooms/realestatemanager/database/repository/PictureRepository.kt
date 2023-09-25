package com.openclassrooms.realestatemanager.database.repository

import com.openclassrooms.realestatemanager.database.dao.PictureDao
import com.openclassrooms.realestatemanager.database.entity.Picture
import com.openclassrooms.realestatemanager.database.utils.PictureTuple

class PictureRepository(private val pictureDao: PictureDao) {

    suspend fun insertPicture(picture: Picture) {
        pictureDao.insertPicture(picture)
    }

    suspend fun deletePicture(propertyId: Int, uri: String, title: String) {
        pictureDao.deletePicture(propertyId, uri, title)
    }

    fun getPictureFromPropertyId(id: Int): MutableList<PictureTuple> {
        return pictureDao.getPictureFromPropertyId(id)
    }
}