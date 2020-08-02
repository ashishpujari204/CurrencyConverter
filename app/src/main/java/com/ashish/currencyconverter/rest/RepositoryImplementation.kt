package com.ashish.currencyconverter.rest

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.ashish.currencyconverter.room.RateDAO
import com.ashish.currencyconverter.ui.home.CurrencyViewModel
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

open class RepositoryImplementation(private var apiInterface: ApiInterface,
                                    private val rateDAO: RateDAO,
                                    androidApplication: Application) {

    fun getCurrencyCodes(base: String): MutableLiveData<String> {
        val userData = MutableLiveData<String>()
        CoroutineScope(Dispatchers.Main + handler).launch {
            val response =  apiInterface.getData(base).await()
            userData.value = response.body().toString()
            parseJson(response.body().toString(), rateDAO)
        }
        return userData
    }

    private fun parseJson(response: String, rateDAO: RateDAO) {
        val currencyViewModel = CurrencyViewModel(this, rateDAO)
        currencyViewModel.insert(currencyViewModel.parseJson(response))
    }


    private val handler = CoroutineExceptionHandler { _, exception ->
        when (exception) {

            is HttpException -> {
                Toast.makeText(androidApplication,
                    (getErrorMessage(exception.code())),
                    Toast.LENGTH_SHORT).show()
            }
            is SocketTimeoutException -> {

                Toast.makeText(androidApplication,
                    getErrorMessage(ErrorCodes.SocketTimeOut.code),
                    Toast.LENGTH_SHORT).show()

            }
            is TimeoutException -> {
                Toast.makeText(androidApplication,
                    getErrorMessage(ErrorCodes.TimeOutException.code),
                    Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(androidApplication,
                    (getErrorMessage(Int.MAX_VALUE)),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getErrorMessage(code: Int): String {
        return when (code) {
            ErrorCodes.SocketTimeOut.code -> "Timeout"
            ErrorCodes.TimeOutException.code -> "Timeout"
            401 -> "Unauthorised"
            404 -> "Not found"
            else -> "Something went wrong"
        }
    }

    enum class ErrorCodes(val code: Int) {
        SocketTimeOut(-1),
        TimeOutException(-2)
    }
}