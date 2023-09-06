package com.openclassrooms.realestatemanager.database

import android.net.Uri

data class PropertyState(
    val property: List<Property> = emptyList(),
    val type: PropertyType = PropertyType.HOUSE,
    val price: Int = 0,
    val surface: Int = 0,
    val pieceNumber: Int = 0,
    val description: String = "",
    val picture: MutableList<Uri> = mutableListOf(),
    val address: String = "",
    val location: String = "",
    val nearInterestPoint: MutableList<InterestPoint> = mutableListOf(),
    val status: Status = Status.AVAILABLE,
    val entryDate: String = "",
    val soldDate: String = "",
    val agent: Agent = Agent.STEPHANE_PLAZA,
    val isAddingProperty: Boolean = false,
    val sortType: SortType = SortType.ENTRY_DATE
)
