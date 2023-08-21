package com.openclassrooms.realestatemanager.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Upsert

@Dao
interface PropertyDao {

    @Insert
    suspend fun insertProperty(property: Property)

    @Upsert
    suspend fun upsertProperty(property: Property)
}