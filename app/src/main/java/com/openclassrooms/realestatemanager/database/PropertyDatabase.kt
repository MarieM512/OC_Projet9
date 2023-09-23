package com.openclassrooms.realestatemanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.database.entity.NearInterestPoint
import com.openclassrooms.realestatemanager.database.dao.NearInterestPointDao
import com.openclassrooms.realestatemanager.database.entity.Property
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.utils.Converters

@Database(
    entities = [Property::class, NearInterestPoint::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PropertyDatabase : RoomDatabase() {

    abstract val propertyDao: PropertyDao
    abstract val nearDao: NearInterestPointDao
}
