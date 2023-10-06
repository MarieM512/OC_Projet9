package com.openclassrooms.realestatemanager.database.entity

import android.content.ContentValues
import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.database.utils.Agent
import com.openclassrooms.realestatemanager.database.utils.PropertyType
import com.openclassrooms.realestatemanager.database.utils.Status

@Entity
data class Property(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: PropertyType,
    val price: Int,
    val surface: Int,
    val pieceNumber: Int,
    val description: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val status: Status,
    val entryDate: String,
    val soldDate: String,
    val agent: Agent,
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))

    companion object {
        fun fromContentValues(values: ContentValues): Property {
            return Property(
                id = if (values.containsKey("id")) values.getAsInteger("id") else 0,
                type = (if (values.containsKey("type")) values.get("type") else PropertyType.HOUSE) as PropertyType,
                price = if (values.containsKey("price")) values.getAsInteger("price") else 0,
                surface = if (values.containsKey("surface")) values.getAsInteger("surface") else 0,
                pieceNumber = if (values.containsKey("pieceNumber")) values.getAsInteger("pieceNumber") else 0,
                description = if (values.containsKey("description")) values.getAsString("description") else "",
                address = if (values.containsKey("address")) values.getAsString("address") else "",
                latitude = (if (values.containsKey("latitude")) values.getAsLong("latitude") else 0.0) as Double,
                longitude = (if (values.containsKey("longitude")) values.getAsLong("longitude") else 0.0) as Double,
                status = (if (values.containsKey("status")) values.get("status") else Status.AVAILABLE) as Status,
                entryDate = if (values.containsKey("entryDate")) values.getAsString("entryDate") else "",
                soldDate = if (values.containsKey("soldDate")) values.getAsString("soldDate") else "",
                agent = (if (values.containsKey("agent")) values.get("agent") else Agent.STEPHANE_PLAZA) as Agent,
            )
        }
    }
}
