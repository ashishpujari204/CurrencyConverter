package com.ashish.currencyconverter.ui.home

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.ashish.currencyconverter.R
import com.ashish.currencyconverter.rest.RepositoryImplementation
import com.ashish.currencyconverter.room.CurrencyRepo
import com.ashish.currencyconverter.room.RateDAO
import com.ashish.currencyconverter.util.Constants
import com.ashish.currencyconverter.util.Constants.Companion.CONVERSATION_RATES
import com.ashish.currencyconverter.util.Constants.Companion.DEFAULT_VALUE
import com.ashish.currencyconverter.util.Constants.Companion.FROM_CURRENCY_INPUT
import com.ashish.currencyconverter.util.Constants.Companion.TO_CURRENCY_INPUT
import com.ashish.currencyconverter.util.NavigationUtil
import com.ashish.currencyconverter.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject


open class CurrencyViewModel(application: Application,
                             private val repositoryImplementation: RepositoryImplementation,
                             private val rateDAO: RateDAO) : AndroidViewModel(application) {


    private val repository: CurrencyRepo = CurrencyRepo(rateDAO)
    val newRecords: LiveData<List<RateClass>> = rateDAO.getLiveRecords()
    var fromInputText = ObservableDouble(0.0)
    var uiModelClassObject = UIModelClass("", 0.0, "", 0.0, 0.0)
    var uiModelClassObj = ObservableField<UIModelClass>(uiModelClassObject)

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(rate: List<RateClass>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(rate)
    }

    /**
     * delete all records from database
     */
    private fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.delete()
    }


    /**
     * called network call here and return string response
     */
    fun getCurrencyData(base: String, applicationContext: Context): MutableLiveData<String> {
        return repositoryImplementation.getCurrencyCodes(base, applicationContext)
    }

    /**
     * get currency code from room db.
     */
    private fun getCode(): ArrayList<RateClass> = runBlocking(Dispatchers.Default) {
        val result = withContext(Dispatchers.Default) { repository.getRates() }
        return@runBlocking result as ArrayList<RateClass>
    }

    /**
     * parse response json and return code array
     */
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

    /**
     * on text changed method is called when user enter from amount
     */
    fun onAmountChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        try {
            if (s != null && s.isNotEmpty() && getCode().isNotEmpty()) {
                val toObject =
                    getCode().find { it.code == uiModelClassObj.get()?.toCode.toString() }
                val calculatedAmount = s.toString().toDouble() * toObject?.rate!!
                fromInputText.set(Util.roundOffDecimal(calculatedAmount))
            }
        } catch (e: NumberFormatException) {
        } catch (e1: KotlinNullPointerException) {

        }
    }

    /**
     * get From currency code from list
     */
    fun getFromCurrencyCode(context: Context) {
        if (Util.verifyAvailableNetwork(context as AppCompatActivity)) {
            NavigationUtil.pickCurrencyCode(context, FROM_CURRENCY_INPUT)
        } else {
            Toast.makeText(context as Activity,
                context.resources.getString(R.string.network_connection),
                Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * select To currency code from list
     */
    fun getToCurrencyCode(context: Context) {
        NavigationUtil.pickCurrencyCode(context as AppCompatActivity, TO_CURRENCY_INPUT)
    }

}