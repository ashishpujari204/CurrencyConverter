package com.ashish.currencyconverter.rest

import android.content.Context
import androidx.lifecycle.MutableLiveData

interface Repository {
    fun getCurrencyCodes(base : String,context: Context) : MutableLiveData<String>
}