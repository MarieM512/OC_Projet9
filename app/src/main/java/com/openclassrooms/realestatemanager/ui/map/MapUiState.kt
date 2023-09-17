package com.openclassrooms.realestatemanager.ui.map

import com.google.android.gms.maps.model.LatLng

data class MapUiState(
    val currentLocation: LatLng = LatLng(0.0, 0.0)
)
