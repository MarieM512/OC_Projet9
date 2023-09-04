package com.openclassrooms.realestatemanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Property::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class PropertyDatabase : RoomDatabase() {

    abstract val dao: PropertyDao
}
