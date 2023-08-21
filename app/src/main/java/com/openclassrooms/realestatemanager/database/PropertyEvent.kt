package com.openclassrooms.realestatemanager.database

sealed interface PropertyEvent {
    object SaveProperty: PropertyEvent
//    data class SetType(val type: PropertyType): PropertyEvent
    data class SetType(val type: String): PropertyEvent
    data class SetPrice(val price: Int): PropertyEvent
    data class SetSurface(val surface: Int): PropertyEvent
    data class SetPieceNumber(val pieceNumber: Int): PropertyEvent
    data class SetDescription(val description: String): PropertyEvent
//    data class SetPicture(val picture: Picture): PropertyEvent
    data class SetPicture(val picture: String): PropertyEvent
    data class SetAddress(val address: String): PropertyEvent
    data class SetLocation(val location: String): PropertyEvent
//    data class SetNearInterestPoint(val nearInterestPoint: List<InterestPoint>): PropertyEvent
    data class SetNearInterestPoint(val nearInterestPoint: String): PropertyEvent
//    data class SetStatus(val status: Status): PropertyEvent
    data class SetStatus(val status: String): PropertyEvent
    data class SetEntryDate(val entryDate: String): PropertyEvent
    data class SetSoldDate(val soldDate: String): PropertyEvent
//    data class SetAgent(val agent: Agent): PropertyEvent
    data class SetAgent(val agent: String): PropertyEvent
    object ShowDialog: PropertyEvent
    object HideDialog: PropertyEvent
    data class SortProperty(val sortType: SortType): PropertyEvent

}