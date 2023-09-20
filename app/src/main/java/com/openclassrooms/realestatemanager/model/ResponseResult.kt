package com.openclassrooms.realestatemanager.model

import com.google.gson.annotations.SerializedName

data class ResponseResult(
    @SerializedName("results")
    val results: List<Address>
)
