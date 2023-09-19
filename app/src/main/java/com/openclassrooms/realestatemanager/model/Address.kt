package com.openclassrooms.realestatemanager.model

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("formatted")
    val address: String,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("lat")
    val lat: Double,
)
