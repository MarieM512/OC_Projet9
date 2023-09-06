package com.openclassrooms.realestatemanager.utils

import android.net.Uri
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.database.InterestPoint

@ProvidedTypeConverter
class Converters {

    @TypeConverter
    fun fromUriList(value: List<Uri>): String {
        val gson = GsonBuilder().registerTypeAdapter(Uri::class.java, UriTypeAdapter).create()
        val listType = object : TypeToken<List<Uri>>() {}.type
        return gson.toJson(value, listType)
    }

    @TypeConverter
    fun toUriList(value: String): List<Uri> {
        val gson = GsonBuilder().registerTypeAdapter(Uri::class.java, UriTypeAdapter).create()
        val listType = object : TypeToken<List<Uri>>() {}.type
        return gson.fromJson(value, listType)
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
