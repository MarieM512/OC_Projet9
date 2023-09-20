package com.openclassrooms.realestatemanager.ViewModel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.database.Agent
import com.openclassrooms.realestatemanager.database.InterestPoint
import com.openclassrooms.realestatemanager.database.Property
import com.openclassrooms.realestatemanager.database.PropertyDao
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.database.PropertyType
import com.openclassrooms.realestatemanager.database.SortType
import com.openclassrooms.realestatemanager.database.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class PropertyViewModel(
    private val dao: PropertyDao,
) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.RESET)
    private val _properties = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.RESET -> dao.getAllProperties()
                SortType.SURFACE -> dao.getPropertyFilteredBySurface(_state.value.minSurface, _state.value.maxSurface)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(PropertyState())

    val state = combine(_state, _sortType, _properties) { state, sortType, properties ->
        state.copy(
            property = properties,
            sortType = sortType,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PropertyState())

    @SuppressLint("SimpleDateFormat")
    fun onEvent(event: PropertyEvent) {
        when (event) {
            //
            PropertyEvent.DeleteAllProperty -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dao.nukeTable()
                }
            }

            is PropertyEvent.FilterBySurfaceMin -> {
                _state.update {
                    it.copy(
                        minSurface = event.min,
                    )
                }
            }

            is PropertyEvent.FilterBySurfaceMax -> {
                _state.update {
                    it.copy(
                        maxSurface = event.max,
                    )
                }
            }

            PropertyEvent.ResetFilter -> {
                _state.update {
                    it.copy(
                        minSurface = 0,
                        maxSurface = 0,
                    )
                }
            }

            PropertyEvent.Reset -> {
                _state.update {
                    it.copy(
                        type = PropertyType.HOUSE,
                        price = 0,
                        surface = 0,
                        pieceNumber = 0,
                        description = "",
                        uriPicture = mutableListOf(),
                        titlePicture = mutableListOf(),
                        address = "",
                        latitude = 0.0,
                        longitude = 0.0,
                        nearInterestPoint = mutableListOf(),
                        status = Status.AVAILABLE,
                        entryDate = "",
                        soldDate = "",
                        agent = Agent.STEPHANE_PLAZA,
                    )
                }
            }

            is PropertyEvent.SaveProperty -> {
                val id = event.id
                val type = _state.value.type
                val price = _state.value.price
                val surface = _state.value.surface
                val pieceNumber = _state.value.pieceNumber
                val description = _state.value.description
                val uriPicture = _state.value.uriPicture
                val titlePicture = _state.value.titlePicture
                val address = _state.value.address
                val latitude = _state.value.latitude
                val longitude = _state.value.longitude
                val nearInterestPoint = _state.value.nearInterestPoint
                val status = _state.value.status
                val soldDate = _state.value.soldDate
                val agent = _state.value.agent

                val property: Property

                if (id != -1) {
                    property = Property(
                        id = id,
                        type = type,
                        price = price,
                        surface = surface,
                        pieceNumber = pieceNumber,
                        description = description,
                        uriPicture = uriPicture,
                        titlePicture = titlePicture,
                        address = address,
                        latitude = latitude,
                        longitude = longitude,
                        nearInterestPoint = nearInterestPoint,
                        status = status,
                        entryDate = SimpleDateFormat("dd/MM/yyyy").format(Date()),
                        soldDate = soldDate,
                        agent = agent,
                    )
                } else {
                    property = Property(
                        type = type,
                        price = price,
                        surface = surface,
                        pieceNumber = pieceNumber,
                        description = description,
                        uriPicture = uriPicture,
                        titlePicture = titlePicture,
                        address = address,
                        latitude = latitude,
                        longitude = longitude,
                        nearInterestPoint = nearInterestPoint,
                        status = status,
                        entryDate = SimpleDateFormat("dd/MM/yyyy").format(Date()),
                        soldDate = soldDate,
                        agent = agent,
                    )
                }
                viewModelScope.launch {
                    dao.upsertProperty(property)
                }
                _state.update {
                    it.copy(
                        type = PropertyType.HOUSE,
                        price = 0,
                        surface = 0,
                        pieceNumber = 0,
                        description = "",
                        uriPicture = mutableListOf(),
                        titlePicture = mutableListOf(),
                        address = "",
                        latitude = 0.0,
                        longitude = 0.0,
                        nearInterestPoint = mutableListOf(),
                        status = Status.AVAILABLE,
                        entryDate = "",
                        soldDate = "",
                        agent = Agent.STEPHANE_PLAZA,
                    )
                }
            }

            is PropertyEvent.SetAddress -> {
                _state.update {
                    it.copy(
                        address = event.address,
                    )
                }
            }

            is PropertyEvent.SetAgent -> {
                _state.update {
                    it.copy(
                        agent = event.agent,
                    )
                }
            }

            is PropertyEvent.SetDescription -> {
                _state.update {
                    it.copy(
                        description = event.description,
                    )
                }
            }

            is PropertyEvent.SetLatitude -> {
                _state.update {
                    it.copy(
                        latitude = event.latitude,
                    )
                }
            }

            is PropertyEvent.SetLongitude -> {
                _state.update {
                    it.copy(
                        longitude = event.longitude,
                    )
                }
            }

            is PropertyEvent.SetNearInterestPoint -> {
                _state.update {
                    val near: MutableList<InterestPoint> = it.nearInterestPoint
                    if (near.contains(event.nearInterestPoint)) {
                        near.remove(event.nearInterestPoint)
                    } else {
                        near.add(event.nearInterestPoint)
                    }
                    it.copy(nearInterestPoint = near)
                }
            }

            is PropertyEvent.SetUriPicture -> {
                _state.update {
                    val image: MutableList<String> = it.uriPicture
                    if (image.contains(event.uriPicture)) {
                        image.remove(event.uriPicture)
                    } else {
                        image.add(event.uriPicture)
                    }
                    it.copy(uriPicture = image)
                }
            }

            is PropertyEvent.SetTitlePicture -> {
                _state.update {
                    val title: MutableList<String> = it.titlePicture
                    if (title.contains(event.titlePicture)) {
                        title.remove(event.titlePicture)
                    } else {
                        title.add(event.titlePicture)
                    }
                    it.copy(titlePicture = title)
                }
            }

            is PropertyEvent.SetPieceNumber -> {
                _state.update {
                    it.copy(
                        pieceNumber = event.pieceNumber,
                    )
                }
            }

            is PropertyEvent.SetPrice -> {
                _state.update {
                    it.copy(
                        price = event.price,
                    )
                }
            }

            is PropertyEvent.SetSoldDate -> {
                _state.update {
                    it.copy(
                        soldDate = event.soldDate,
                    )
                }
            }

            is PropertyEvent.SetStatus -> {
                _state.update {
                    it.copy(
                        status = event.status,
                    )
                }
            }

            is PropertyEvent.SetSurface -> {
                _state.update {
                    it.copy(
                        surface = event.surface,
                    )
                }
            }

            is PropertyEvent.SetType -> {
                _state.update {
                    it.copy(
                        type = event.type,
                    )
                }
            }

            is PropertyEvent.SortProperty -> {
                _sortType.value = event.sortType
            }
        }
    }
}
