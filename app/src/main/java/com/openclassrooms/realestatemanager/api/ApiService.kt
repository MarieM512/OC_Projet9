package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.Address

interface ApiService {
    fun getAddressList(addressList: List<Address>)
}
