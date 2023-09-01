package com.openclassrooms.realestatemanager.ui.add

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.Agent
import com.openclassrooms.realestatemanager.database.InterestPoint
import com.openclassrooms.realestatemanager.database.PropertyType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddUiState())
    val uiState: StateFlow<AddUiState> = _uiState.asStateFlow()

    fun updateType(value: PropertyType) {
        _uiState.update {
            it.copy(type = value)
        }
    }

    fun updateSurface(value: Int) {
        _uiState.update {
            it.copy(surface = value)
        }
    }

    fun updatePieceNumber(value: Int) {
        _uiState.update {
            it.copy(pieceNumber = value)
        }
    }

    fun updateAddress(value: String) {
        _uiState.update {
            it.copy(address = value)
        }
    }

    fun updatePrice(value: Int) {
        _uiState.update {
            it.copy(price = value)
        }
    }

    fun updateAgent(value: Agent) {
        _uiState.update {
            it.copy(agent = value)
        }
    }

    fun updateDescription(value: String) {
        _uiState.update {
            it.copy(description = value)
        }
    }

    fun updateInterestPoint(value: InterestPoint) {
        _uiState.update {
            val near: MutableList<InterestPoint> = it.nearInterestPoint
            if (near.contains(value)) {
                near.remove(value)
            } else {
                near.add(value)
            }
            it.copy(nearInterestPoint = near)
        }
    }

    fun addProperty() {
        println("near: ${uiState.value.nearInterestPoint} - type: ${uiState.value.type} - price: ${uiState.value.price} - surface: ${uiState.value.surface} - number: ${uiState.value.pieceNumber} - agent: ${uiState.value.agent} - address: ${uiState.value.address} - description: ${uiState.value.description}")
    }
}
