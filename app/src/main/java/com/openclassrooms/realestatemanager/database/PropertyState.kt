package com.openclassrooms.realestatemanager.database

data class PropertyState(
    val property: List<Property> = emptyList(),
//    val type: PropertyType = PropertyType.DETACHED_HOUSE,
    val type: String = "",
    val price: Int = 0,
    val surface: Int = 0,
    val pieceNumber: Int = 0,
    val description: String = "",
//    val picture: Picture = Picture(0, 0, ""),
    val picture: String = "",
    val address: String = "",
    val location: String = "",
//    val nearInterestPoint: List<InterestPoint> = emptyList(),
    val nearInterestPoint: String = "",
//    val status: Status = Status.AVAILABLE,
    val status: String = "",
    val entryDate: String = "",
    val soldDate: String = "",
//    val agent: Agent = Agent.STEPHANE_PLAZA,
    val agent: String = "",
    val isAddingProperty: Boolean = false,
    val sortType: SortType = SortType.ENTRY_DATE
)
