package com.openclassrooms.realestatemanager.database.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity
data class Picture(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val propertyId: Int,
    val uri: String,
    val title: String
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}