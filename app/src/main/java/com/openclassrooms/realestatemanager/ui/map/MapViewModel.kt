package com.openclassrooms.realestatemanager.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

class MapViewModel : ViewModel() {
    lateinit var fusedLocationClient: FusedLocationProviderClient

    private val _uiState = MutableStateFlow(MapUiState())
    private val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    fun getCurrentLocation(camera: CameraPositionState) {
        fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
            .addOnSuccessListener { location ->
                _uiState.update {
                    it.copy(
                        currentLocation = LatLng(location.latitude, location.longitude),
                    )
                }
                viewModelScope.launch {
                    try {
                        camera.animate(CameraUpdateFactory.newLatLng(uiState.value.currentLocation))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                println(uiState.value.currentLocation)
            }
    }
}
