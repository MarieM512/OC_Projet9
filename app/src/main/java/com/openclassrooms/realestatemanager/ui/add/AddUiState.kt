package com.openclassrooms.realestatemanager.ui.add

import com.openclassrooms.realestatemanager.model.Address

data class AddUiState(
    val address: String = "",
    val addressList: List<Address> = emptyList(),
)
