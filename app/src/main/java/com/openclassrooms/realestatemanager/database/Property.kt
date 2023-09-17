package com.openclassrooms.realestatemanager.database

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity
data class Property(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: PropertyType,
    val price: Int,
    val surface: Int,
    val pieceNumber: Int,
    val description: String,
    val uriPicture: List<String>,
    val titlePicture: List<String>,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val nearInterestPoint: List<InterestPoint>,
    val status: Status,
    val entryDate: String,
    val soldDate: String,
    val agent: Agent,
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}
