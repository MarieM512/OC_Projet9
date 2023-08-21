package com.openclassrooms.realestatemanager.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Property(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
//    val type: PropertyType,
    val type: String,
    val price: Int,
    val surface: Int,
    val pieceNumber: Int,
    val description: String,
//    val picture: Picture,
    val picture: String,
    val address: String,
    val location: String,
//    val nearInterestPoint: List<InterestPoint>,
    val nearInterestPoint: String,
//    val status: Status,
    val status: String,
    val entryDate: String,
    val soldDate: String,
//    val agent: Agent
    val agent: String
)
