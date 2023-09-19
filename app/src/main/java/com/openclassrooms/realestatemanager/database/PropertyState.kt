package com.openclassrooms.realestatemanager.database

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
    val nearInterestPoint: MutableList<InterestPoint> = mutableListOf(),
    val status: Status = Status.AVAILABLE,
    val entryDate: String = "",
    val soldDate: String = "",
    val agent: Agent = Agent.STEPHANE_PLAZA,
    val sortType: SortType = SortType.RESET,

    val minSurface: String = "",
    val maxSurface: String = "",
)
