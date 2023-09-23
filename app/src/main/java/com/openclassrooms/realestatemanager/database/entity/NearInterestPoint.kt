package com.openclassrooms.realestatemanager.database.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "nearInterestPoint")
data class NearInterestPoint(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val propertyId: Int,
    val nearInterestPoint: String,
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}
