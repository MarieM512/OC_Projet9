package com.openclassrooms.realestatemanager.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.database.InterestPoint

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromInterestPointList(value: List<InterestPoint>): String {
        val listType = object : TypeToken<List<InterestPoint>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toInterestPointList(value: String): List<InterestPoint> {
        val listType = object : TypeToken<List<InterestPoint>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
