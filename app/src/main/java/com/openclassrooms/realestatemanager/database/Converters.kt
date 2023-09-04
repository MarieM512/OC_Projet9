package com.openclassrooms.realestatemanager.database

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromUriList(value: List<Uri>): String {
        val listType = object : TypeToken<List<Uri>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toUriList(value: String): List<Uri> {
        val listType = object : TypeToken<List<Uri>>() {}.type
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
