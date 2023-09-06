package com.openclassrooms.realestatemanager.database

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Property(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: PropertyType,
    val price: Int,
    val surface: Int,
    val pieceNumber: Int,
    val description: String,
    val picture: List<Uri>,
    val address: String,
    val location: String,
    val nearInterestPoint: List<InterestPoint>,
    val status: Status,
    val entryDate: String,
    val soldDate: String,
    val agent: Agent,
)
