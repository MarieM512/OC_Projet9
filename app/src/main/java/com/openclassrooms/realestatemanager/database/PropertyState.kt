package com.openclassrooms.realestatemanager.database

import com.openclassrooms.realestatemanager.database.entity.Property
import com.openclassrooms.realestatemanager.database.utils.Agent
import com.openclassrooms.realestatemanager.database.utils.PropertyDate
import com.openclassrooms.realestatemanager.database.utils.PropertyType
import com.openclassrooms.realestatemanager.database.utils.Status

data class PropertyState(
    val property: List<Property> = emptyList(),
    val type: PropertyType = PropertyType.HOUSE,
    val price: Int = 0,
    val surface: Int = 0,
    val pieceNumber: Int = 0,
    val description: String = "",
    val uriPicture: MutableList<String> = mutableListOf(),
    val titlePicture: MutableList<String> = mutableListOf(),
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val nearInterestPoint: MutableList<String> = mutableListOf(),
    val status: Status = Status.AVAILABLE,
    val entryDate: String = "",
    val soldDate: String = "",
    val agent: Agent = Agent.STEPHANE_PLAZA,
    val sortType: SortType = SortType.RESET,

    val minSurface: Int = 0,
    val maxSurface: Int = 10000,
    val minPrice: Int = 0,
    val maxPrice: Int = 1000000000,
    val filterAgent: Agent? = null,
    val filterAddress: String = "",
    val filterType: PropertyType? = null,
    val minPiece: Int = 0,
    val maxPiece: Int = 1000,
    val filterPicture: Int = 1,
    val filterEntryDate: PropertyDate? = null,
    val filterSoldDate: PropertyDate? = null,
    val filterNear: MutableList<String> = mutableListOf(),

    val isCreated: Boolean = false,
)
