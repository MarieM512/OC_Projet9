package com.openclassrooms.realestatemanager.database

import com.openclassrooms.realestatemanager.database.utils.Agent
import com.openclassrooms.realestatemanager.database.utils.PropertyDate
import com.openclassrooms.realestatemanager.database.utils.PropertyType
import com.openclassrooms.realestatemanager.database.utils.Status

sealed interface PropertyEvent {
    data class SaveProperty(val id: Int) : PropertyEvent
    object Reset : PropertyEvent

    // Filter
    data class FilterBySurfaceMin(val min: Int) : PropertyEvent
    data class FilterBySurfaceMax(val max: Int) : PropertyEvent
    data class FilterByPriceMin(val min: Int) : PropertyEvent
    data class FilterByPriceMax(val max: Int) : PropertyEvent
    data class FilterByAgent(val agent: Agent) : PropertyEvent
    data class FilterByAddress(val address: String) : PropertyEvent
    data class FilterByType(val type: PropertyType) : PropertyEvent
    data class FilterByPieceMin(val min: Int) : PropertyEvent
    data class FilterByPieceMax(val max: Int) : PropertyEvent
    data class FilterByPicture(val number: Int) : PropertyEvent
    data class FilterByEntryDate(val date: PropertyDate) : PropertyEvent
    data class FilterBySoldDate(val date: PropertyDate) : PropertyEvent
    data class FilterByNear(val near: String) : PropertyEvent
    object ResetFilter : PropertyEvent

    data class SetType(val type: PropertyType) : PropertyEvent
    data class SetPrice(val price: Int) : PropertyEvent
    data class SetSurface(val surface: Int) : PropertyEvent
    data class SetPieceNumber(val pieceNumber: Int) : PropertyEvent
    data class SetDescription(val description: String) : PropertyEvent
    data class SetUriPicture(val uriPicture: String) : PropertyEvent
    data class SetTitlePicture(val titlePicture: String) : PropertyEvent
    data class SetAddress(val address: String) : PropertyEvent
    data class SetLatitude(val latitude: Double) : PropertyEvent
    data class SetLongitude(val longitude: Double) : PropertyEvent
    data class SetEntryDate(val date: String) : PropertyEvent

    data class SetNearInterestPoint(val nearInterestPoint: String) : PropertyEvent

    data class SetStatus(val status: Status) : PropertyEvent
    data class SetSoldDate(val soldDate: String) : PropertyEvent
    data class SetAgent(val agent: Agent) : PropertyEvent
    data class SortProperty(val sortType: SortType) : PropertyEvent

    object ResetCreated : PropertyEvent
}
