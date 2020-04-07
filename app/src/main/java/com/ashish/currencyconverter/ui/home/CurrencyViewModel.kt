package com.ashish.currencyconverter.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ashish.currencyconverter.rest.ApiClient
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyViewModel : AndroidViewModel {
    constructor(application: Application) : super(application) {
    }

    fun getCurrencyData(base : String): MutableLiveData<JsonObject> {
        var userData = MutableLiveData<JsonObject>()
        val dataCall: Call<JsonObject> =
            ApiClient.getClient.getData(base)
        dataCall!!.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        userData.value = response.body()
                    } else {
                        userData.value = null
                    }
                } else {
                    userData.value = null
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                userData.value = null
            }
        })
        return userData
    }
}