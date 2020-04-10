package com.ashish.currencyconverter.ui.home

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ashish.currencyconverter.R
import com.ashish.currencyconverter.rest.RepositoryImplementation
import com.ashish.currencyconverter.room.CurrencyRepo
import com.ashish.currencyconverter.room.CurrencyRoomDatabase
import com.ashish.currencyconverter.util.Constants
import com.ashish.currencyconverter.util.Constants.Companion.CONVERSATION_RATES
import com.ashish.currencyconverter.util.Constants.Companion.DEFAULT_VALUE
import com.ashish.currencyconverter.util.Constants.Companion.FROM_CURRENCY_INPUT
import com.ashish.currencyconverter.util.Constants.Companion.TO_CURRENCY_INPUT
import com.ashish.currencyconverter.util.NavigationUtil
import com.ashish.currencyconverter.util.Util
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CurrencyRepo
    val newRecords: LiveData<List<RateClass>>
    private val apiRepository: RepositoryImplementation

    init {
        val rateDAO = CurrencyRoomDatabase.getDatabase(application).rateDAO()
        repository = CurrencyRepo(rateDAO)
        apiRepository = RepositoryImplementation()
        newRecords = rateDAO.getLiveRecords()
    }

    private fun getCode(): ArrayList<RateClass> = runBlocking(Dispatchers.Default) {
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


    fun getCurrencyData(base: String, applicationContext: Context): MutableLiveData<String> {
        return apiRepository.getCurrencyCodes(base, applicationContext)
    }

    fun swipeCurrencyCode(base: String, formCode: String, context: Context) {
        Constants.saveFromCode(context as Activity, base)
        Constants.saveToCode(context as Activity, formCode)
        getCurrencyData(base, context)

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

    fun onAmountTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        /*try {
            if (s != null && s.isNotEmpty() && getCode().isNotEmpty()) {
                var toObject = getToCurrencyObject()
                var calculatedAmount = s.toString().toDouble() * toObject?.rate!!
                tvToInput.text = "" + Util.roundOffDecimal(calculatedAmount)
            }
        } catch (e: NumberFormatException) {
        }*/
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