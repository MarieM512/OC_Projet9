package com.openclassrooms.realestatemanager.database.repository

import com.openclassrooms.realestatemanager.database.dao.NearInterestPointDao
import com.openclassrooms.realestatemanager.database.entity.NearInterestPoint

class NearInterestPointRepository(private val nearInterestPointDao: NearInterestPointDao) {

    suspend fun insertNearInterestPoint(nearInterestPoint: NearInterestPoint) {
        nearInterestPointDao.insertNearInterestPoint(nearInterestPoint)
    }

    suspend fun deleteNearInterestPoint(propertyId: Int, near: String) {
        nearInterestPointDao.deleteNearInterestPoint(propertyId, near)
    }

    fun getNearInterestPointFromPropertyId(id: Int): MutableList<String> {
        return nearInterestPointDao.getNearInterestPointFromPropertyId(id)
    }
}