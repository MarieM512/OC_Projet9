package com.openclassrooms.realestatemanager.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.database.Agent
import com.openclassrooms.realestatemanager.database.Picture
import com.openclassrooms.realestatemanager.database.Property
import com.openclassrooms.realestatemanager.database.PropertyDao
import com.openclassrooms.realestatemanager.database.PropertyEvent
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.database.PropertyType
import com.openclassrooms.realestatemanager.database.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PropertyViewModel(
    private val dao: PropertyDao,
) : ViewModel() {

    private val _state = MutableStateFlow(PropertyState())

    fun onEvent(event: PropertyEvent) {
        when (event) {
            PropertyEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingProperty = false,
                    )
                }
            }

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
                val entryDate = _state.value.entryDate
                val soldDate = _state.value.soldDate
                val agent = _state.value.agent

                if (
                    description.isBlank() || address.isBlank() || location.isBlank() || entryDate.isBlank() || soldDate.isBlank()
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
                    entryDate = entryDate,
                    soldDate = soldDate,
                    agent = agent
                )
                viewModelScope.launch {
                    dao.upsertProperty(property)
                }
                _state.update { it.copy(
                    isAddingProperty = false,
                    type = PropertyType.DETACHED_HOUSE,
                    price = 0,
                    surface = 0,
                    pieceNumber = 0,
                    description = "",
                    picture = Picture(0,0, ""),
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

            is PropertyEvent.SetEntryDate -> {
                _state.update {
                    it.copy(
                        entryDate = event.entryDate,
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

            PropertyEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingProperty = true,
                    )
                }
            }
        }
    }
}
