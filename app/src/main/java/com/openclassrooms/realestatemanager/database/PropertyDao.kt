package com.openclassrooms.realestatemanager.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Upsert
    suspend fun upsertProperty(property: Property)

    @Query("SELECT * FROM property ORDER BY entryDate ASC")
    fun getPropertiesOrderedByEntryDate(): Flow<List<Property>>

    @Query("DELETE FROM property")
    fun nukeTable()
}