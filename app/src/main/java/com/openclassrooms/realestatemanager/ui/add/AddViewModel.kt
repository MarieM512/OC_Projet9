package com.openclassrooms.realestatemanager.ui.add

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.model.ResponseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AddViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddUiState())
    val uiState: StateFlow<AddUiState> = _uiState.asStateFlow()

    fun updateAddress(value: String) {
        _uiState.update {
            it.copy(
                address = value,
            )
        }
    }

    fun getAddressList() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.geoapify.com/v1/geocode/autocomplete?text=${uiState.value.address}&type=city&format=json&apiKey=342761ffcef34fe2b6ea8e0b468ddc4b")
            .get()
            .addHeader("accept", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val addressList = Gson().fromJson(response.body?.string(), ResponseResult::class.java)
                _uiState.update {
                    it.copy(
                        addressList = addressList.results,
                    )
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
}
