package com.openclassrooms.realestatemanager.utils

import com.google.gson.Gson
import com.openclassrooms.realestatemanager.database.Property

class PropertyArgType : JsonNavType<Property>() {

    override fun fromJsonParse(value: String): Property = Gson().fromJson(value, Property::class.java)

    override fun Property.getJsonParse(): String = Gson().toJson(this)
}
