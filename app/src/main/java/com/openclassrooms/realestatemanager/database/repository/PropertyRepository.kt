package com.openclassrooms.realestatemanager.database.repository

import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.database.entity.Property
import com.openclassrooms.realestatemanager.database.utils.Agent
import com.openclassrooms.realestatemanager.database.utils.PropertyType
import kotlinx.coroutines.flow.Flow

class PropertyRepository(private val propertyDao: PropertyDao) {

    suspend fun upsertProperty(property: Property): Long {
        return propertyDao.upsertProperty(property)
    }

    fun getAllProperties(): Flow<List<Property>> {
        return propertyDao.getAllProperties()
    }

    fun getPropertyFiltered(minSurface: Int, maxSurface: Int, minPrice: Int, maxPrice: Int, agent: Agent?, address: String, type: PropertyType?, minPiece: Int, maxPiece: Int, picture: Int, entryDate: String?, soldDate: String?, near1: String?, near2: String?, near3: String?): Flow<List<Property>> {
        return propertyDao.getPropertyFiltered(minSurface, maxSurface, minPrice, maxPrice, agent, address, type, minPiece, maxPiece, picture, entryDate, soldDate, near1, near2, near3)
    }
}