package com.openclassrooms.realestatemanager.utils

import android.os.Bundle
import androidx.navigation.NavType

abstract class JsonNavType<Property> : NavType<Property>(isNullableAllowed = false) {
    abstract fun fromJsonParse(value: String): Property
    abstract fun Property.getJsonParse(): String

    override fun get(bundle: Bundle, key: String): Property? =
        bundle.getString(key)?.let { parseValue(it) }

    override fun parseValue(value: String): Property = fromJsonParse(value)

    override fun put(bundle: Bundle, key: String, value: Property) {
        bundle.putString(key, value.getJsonParse())
    }
}
