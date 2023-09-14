package com.openclassrooms.realestatemanager.database

sealed interface PropertyEvent {
    object SaveProperty : PropertyEvent

    // TO DELETE
    object DeleteAllProperty : PropertyEvent
    data class SetType(val type: PropertyType) : PropertyEvent
    data class SetPrice(val price: Int) : PropertyEvent
    data class SetSurface(val surface: Int) : PropertyEvent
    data class SetPieceNumber(val pieceNumber: Int) : PropertyEvent
    data class SetDescription(val description: String) : PropertyEvent
    data class SetUriPicture(val uriPicture: String) : PropertyEvent
    data class SetTitlePicture(val titlePicture: String) : PropertyEvent
    data class SetAddress(val address: String) : PropertyEvent
    data class SetLocation(val location: String) : PropertyEvent
    data class SetNearInterestPoint(val nearInterestPoint: InterestPoint) : PropertyEvent
    data class SetStatus(val status: Status) : PropertyEvent
    data class SetSoldDate(val soldDate: String) : PropertyEvent
    data class SetAgent(val agent: Agent) : PropertyEvent
    data class SortProperty(val sortType: SortType) : PropertyEvent
}
