package com.openclassrooms.realestatemanager.ui.add

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.api.AddressRepository
import com.openclassrooms.realestatemanager.api.ApiService
import com.openclassrooms.realestatemanager.model.Address
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddViewModel : ViewModel(), ApiService {

    private val _uiState = MutableStateFlow(AddUiState())
    val uiState: StateFlow<AddUiState> = _uiState.asStateFlow()
    private val addressRepository = AddressRepository()

    fun updateAddress(value: String) {
        addressRepository.setAddress(value)
        _uiState.update {
            it.copy(
                address = value,
            )
        }
    }

    fun getAddressList() {
        addressRepository.getAddressList(this)
    }

    fun reset() {
        _uiState.update {
            it.copy(
                address = "",
                addressList = emptyList(),
            )
        }
    }

    override fun getAddressList(addressList: List<Address>) {
        _uiState.update {
            it.copy(
                addressList = addressList,
            )
        }
    }
}
