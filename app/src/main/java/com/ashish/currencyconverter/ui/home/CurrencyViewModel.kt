package com.ashish.currencyconverter.ui.home

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ashish.currencyconverter.rest.ApiClient
import com.ashish.currencyconverter.util.Constants.Companion.DEFAULT_VALUE
import com.ashish.currencyconverter.util.Util
import com.google.gson.JsonObject
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyViewModel : AndroidViewModel {
    constructor(application: Application) : super(application) {
    }

    fun getCurrencyData(base : String): MutableLiveData<String> {
        var userData = MutableLiveData<String>()
        val dataCall: Call<JsonObject> =
            ApiClient.getClient.getData(base)
        dataCall!!.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        userData.value = response.body().toString()
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

    fun getMockCountryCode(activity : Activity) : ArrayList<CurrencyClass>{
        var currencyArray= JSONArray(Util.getAssetJsonData(activity))
        var currencyArrayList = ArrayList<CurrencyClass>()
        for (i in 0 until currencyArray.length()) {
            val currencyObjects = currencyArray.getJSONObject(i)
            var keys=currencyObjects.keys()
            while(keys.hasNext()){
                var key=keys.next()
                var currencyObject=currencyObjects.getJSONObject(key)
                var currencyModel=CurrencyClass()
                currencyModel.name=currencyObject.optString("name",DEFAULT_VALUE)
                currencyModel.symbol=currencyObject.optString("symbol",DEFAULT_VALUE)
                currencyModel.symbolNative=currencyObject.optString("symbol_native",DEFAULT_VALUE)
                currencyModel.code=currencyObject.optString("code",DEFAULT_VALUE)
                currencyModel.namePlural=currencyObject.optString("name_plural",DEFAULT_VALUE)
                currencyArrayList.add(currencyModel)
            }
        }
        return currencyArrayList
    }
}