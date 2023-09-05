package com.openclassrooms.realestatemanager.ViewModel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.database.Agent
import com.openclassrooms.realestatemanager.database.Property
import com.openclassrooms.realestatemanager.database.PropertyDao
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.database.PropertyType
import com.openclassrooms.realestatemanager.database.SortType
import com.openclassrooms.realestatemanager.database.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class PropertyViewModel(
    private val dao: PropertyDao,
) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.ENTRY_DATE)
    private val _properties = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.ENTRY_DATE -> dao.getPropertiesOrderedByEntryDate()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(PropertyState())

    val state = combine(_state, _sortType, _properties) { state, sortType, properties ->
    state.copy(
        property = properties,
        sortType = sortType
    )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PropertyState())

    @SuppressLint("SimpleDateFormat")
    fun onEvent(event: PropertyEvent) {
        when (event) {

            PropertyEvent.SaveProperty -> {
                val type = _state.value.type
                val price = _state.value.price
                val surface = _state.value.surface
                val pieceNumber = _state.value.pieceNumber
                val description = _state.value.description
                val picture = _state.value.picture
                val address = _state.value.address
                val location = _state.value.location
                val nearInterestPoint = _state.value.nearInterestPoint
                val status = _state.value.status
                val soldDate = _state.value.soldDate
                val agent = _state.value.agent

                if (
                    description.isBlank() || address.isBlank()
                ) {
                    return
                }

                val property = Property(
                    type = type,
                    price = price,
                    surface = surface,
                    pieceNumber = pieceNumber,
                    description = description,
                    picture = picture,
                    address = address,
                    location = location,
                    nearInterestPoint = nearInterestPoint,
                    status = status,
                    entryDate = SimpleDateFormat("dd/MM/yyyy").format(Date()),
                    soldDate = soldDate,
                    agent = agent
                )
                viewModelScope.launch {
                    dao.upsertProperty(property)
                }
                _state.update { it.copy(
                    isAddingProperty = false,
                    type = PropertyType.HOUSE,
                    price = 0,
                    surface = 0,
                    pieceNumber = 0,
                    description = "",
                    picture = emptyList(),
                    address = "",
                    location = "",
                    nearInterestPoint = emptyList(),
                    status = Status.AVAILABLE,
                    entryDate = "",
                    soldDate = "",
                    agent = Agent.STEPHANE_PLAZA
                ) }
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

            is PropertyEvent.SetLocation -> {
                _state.update {
                    it.copy(
                        location = event.location,
                    )
                }
            }

            is PropertyEvent.SetNearInterestPoint -> {
                _state.update {
                    it.copy(
                        nearInterestPoint = event.nearInterestPoint,
                    )
                }
            }

            is PropertyEvent.SetPicture -> {
                _state.update {
                    it.copy(
                        picture = event.picture,
                    )
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

            else -> {}
        }
    }
}
