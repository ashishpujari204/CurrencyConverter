package com.ashish.currencyconverter.ui.home

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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


    fun parseJson(response: String): ArrayList<RateClass> {
        val rateCodeArray = ArrayList<RateClass>()
        val jsonObject = JSONObject(response)
        if (jsonObject.optString(Constants.RESULT, DEFAULT_VALUE) == Constants.SUCCESS) {
            deleteAll()
            val rateObject = jsonObject.getJSONObject(CONVERSATION_RATES)
            val keys = rateObject.keys()

            while (keys.hasNext()) {
                val keyValue = keys.next()
                val rateCodeObject = RateClass(0, keyValue, rateObject.optDouble(keyValue, 0.0))
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
            NavigationUtil.pickCurrencyCode(context as AppCompatActivity,FROM_CURRENCY_INPUT)
        } else {
            Toast.makeText(context as Activity,
                context.resources.getString(R.string.network_connection),
                Toast.LENGTH_SHORT).show()
        }
    }

    fun getToCurrencyCode(context: Context) {
        NavigationUtil.pickCurrencyCode(context as AppCompatActivity, TO_CURRENCY_INPUT)

    }


}