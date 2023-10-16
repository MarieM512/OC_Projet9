package com.openclassrooms.realestatemanager.api

import com.google.gson.Gson
import com.openclassrooms.realestatemanager.model.ResponseResult
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class OkhttpService : AddressInterface {

    private var currentAddress: String = ""

    fun getAddressList(apiService: ApiService) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.geoapify.com/v1/geocode/autocomplete?text=$currentAddress&limit=10&format=json&apiKey=342761ffcef34fe2b6ea8e0b468ddc4b")
            .get()
            .addHeader("accept", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val addressList = Gson().fromJson(response.body?.string(), ResponseResult::class.java)
                apiService.getAddressList(addressList.results)
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    override fun getAddress(address: String) {
        currentAddress = address
    }
}
