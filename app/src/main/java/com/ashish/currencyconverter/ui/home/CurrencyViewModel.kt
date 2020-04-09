package com.ashish.currencyconverter.ui.home

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ashish.currencyconverter.R
import com.ashish.currencyconverter.rest.ApiClient
import com.ashish.currencyconverter.room.CurrencyRepo
import com.ashish.currencyconverter.room.CurrencyRoomDatabase
import com.ashish.currencyconverter.util.Constants
import com.ashish.currencyconverter.util.Constants.Companion.CONVERSATION_RATES
import com.ashish.currencyconverter.util.Constants.Companion.DEFAULT_VALUE
import com.ashish.currencyconverter.util.Constants.Companion.FROM_CURRENCY_INPUT
import com.ashish.currencyconverter.util.Constants.Companion.TO_CURRENCY_INPUT
import com.ashish.currencyconverter.util.NavigationUtil
import com.ashish.currencyconverter.util.Util
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_currency_converter.*
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CurrencyRepo
    val newRecords: LiveData<List<RateClass>>
    var codeRateArray: ArrayList<RateClass> = ArrayList()

    init {
        val rateDAO = CurrencyRoomDatabase.getDatabase(application).rateDAO()
        repository = CurrencyRepo(rateDAO)
        newRecords = rateDAO.getLiveRecords()
    }

    fun getCode(): ArrayList<RateClass> = runBlocking(Dispatchers.Default) {
        val result = async { repository.getRates() }.await()
        return@runBlocking result as ArrayList<RateClass>
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(rate: List<RateClass>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(rate)
    }

    private fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.delete()
    }



    fun getCurrencyData(base: String): MutableLiveData<String> {
        var userData = MutableLiveData<String>()
        val dataCall: Call<JsonObject> = ApiClient.getClient.getData(base)
        dataCall!!.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        userData.value = response.body().toString()
                        if(codeRateArray.isNotEmpty()){
                            codeRateArray.clear()
                            codeRateArray.addAll(parseJson(response.body().toString()))
                        }
                        insert(parseJson(response.body().toString()))
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

    fun swipeCurrencyCode(base: String, formCode: String, context: Context) {
        Constants.saveFromCode(context as Activity, base)
        Constants.saveToCode(context as Activity, formCode)
        getCurrencyData(base)

    }

    fun parseJson(response: String): ArrayList<RateClass> {
        var rateCodeArray = ArrayList<RateClass>()
        var jsonObject = JSONObject(response)
        if (jsonObject.optString(Constants.RESULT, DEFAULT_VALUE) == Constants.SUCCESS) {
            deleteAll()
            var rateObject = jsonObject.getJSONObject(CONVERSATION_RATES)
            var keys = rateObject.keys()

            while (keys.hasNext()) {
                var keyValue = keys.next()
                var rateCodeObject = RateClass(0, keyValue, rateObject.optDouble(keyValue, 0.0))
                rateCodeArray.add(rateCodeObject)
            }
        }
        return rateCodeArray
    }

    fun getFromCurrencyCode(context: Context) {
        if (Util.verifyAvailableNetwork(context as AppCompatActivity)) {
            NavigationUtil.pickCurrencyCode(context as AppCompatActivity,
                getMockCountryCode(context as Activity),
                getCode(),
                FROM_CURRENCY_INPUT)
        } else {
            Toast.makeText(context as Activity,
                context.resources.getString(R.string.network_connection),
                Toast.LENGTH_SHORT).show()
        }
    }
    fun getToCurrencyCode(context: Context) {
            NavigationUtil.pickCurrencyCode(context as AppCompatActivity,
                getMockCountryCode(context as Activity),
                getCode(),
                TO_CURRENCY_INPUT)

    }
    fun getMockCountryCode(activity: Activity): ArrayList<CurrencyClass> {
        var currencyArray = JSONArray(Util.getAssetJsonData(activity))
        var currencyArrayList = ArrayList<CurrencyClass>()
        for (i in 0 until currencyArray.length()) {
            val currencyObjects = currencyArray.getJSONObject(i)
            var keys = currencyObjects.keys()
            while (keys.hasNext()) {
                var key = keys.next()
                var currencyObject = currencyObjects.getJSONObject(key)
                var currencyModel = CurrencyClass()
                currencyModel.name = currencyObject.optString("name", DEFAULT_VALUE)
                currencyModel.symbol = currencyObject.optString("symbol", DEFAULT_VALUE)
                currencyModel.symbolNative =
                    currencyObject.optString("symbol_native", DEFAULT_VALUE)
                currencyModel.code = currencyObject.optString("code", DEFAULT_VALUE)
                currencyModel.namePlural = currencyObject.optString("name_plural", DEFAULT_VALUE)
                currencyArrayList.add(currencyModel)
            }
        }
        return currencyArrayList
    }
}