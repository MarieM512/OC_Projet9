package com.openclassrooms.realestatemanager.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Property::class],
    version = 1
)
abstract class PropertyDatabase: RoomDatabase() {

    abstract val dao: PropertyDao
}