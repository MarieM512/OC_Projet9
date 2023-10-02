package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.database.dao.NearInterestPointDao
import com.openclassrooms.realestatemanager.database.dao.PictureDao
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.database.entity.NearInterestPoint
import com.openclassrooms.realestatemanager.database.entity.Picture
import com.openclassrooms.realestatemanager.database.entity.Property

@Database(
    entities = [Property::class, NearInterestPoint::class, Picture::class],
    version = 1,
    exportSchema = false,
)
abstract class PropertyDatabase : RoomDatabase() {

    abstract val propertyDao: PropertyDao
    abstract val nearDao: NearInterestPointDao
    abstract val pictureDao: PictureDao

    companion object {
        @Volatile
        private var INSTANCE: PropertyDatabase? = null

        fun getDatabase(
            context: Context,
        ): PropertyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PropertyDatabase::class.java,
                    "properties.db"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
