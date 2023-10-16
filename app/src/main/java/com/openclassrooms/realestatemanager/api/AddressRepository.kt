package com.openclassrooms.realestatemanager.api

class AddressRepository {

    private val okhttpService = OkhttpService()

    fun setAddress(address: String) {
        okhttpService.getAddress(address)
    }

    fun getAddressList(apiService: ApiService) {
        okhttpService.getAddressList(apiService)
    }
}
