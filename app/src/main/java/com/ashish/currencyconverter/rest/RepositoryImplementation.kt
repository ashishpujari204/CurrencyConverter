package com.ashish.currencyconverter.rest

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ashish.currencyconverter.room.RateDAO
import com.ashish.currencyconverter.ui.home.CurrencyViewModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class RepositoryImplementation(var apiInterface: ApiInterface,val rateDAO: RateDAO) {

    fun getCurrencyCodes(base: String, context: Context): MutableLiveData<String> {
        val userData = MutableLiveData<String>()

        apiInterface.getData(base).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        userData.value = response.body().toString()
                        parseJson(response.body().toString(), context,rateDAO)
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

    private fun parseJson(response: String,
                          context: Context,
                          rateDAO: RateDAO) {
        val currencyViewModel = CurrencyViewModel(context as Application, this,rateDAO)
        currencyViewModel.insert(currencyViewModel.parseJson(response))
    }

}