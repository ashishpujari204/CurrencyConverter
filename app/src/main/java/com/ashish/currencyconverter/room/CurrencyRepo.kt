package com.ashish.currencyconverter.room

import androidx.lifecycle.LiveData
import com.ashish.currencyconverter.ui.home.RateClass

class CurrencyRepo(private val currencyDAO: RateDAO) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
 //   suspend fun   allCodes: List<RateClass> = currencyDAO.getCodes()

    fun getRates() = currencyDAO.getCodes()

    suspend fun insert(rate: List<RateClass>) {
        currencyDAO.insert(rate)
    }

    suspend fun delete() {
        currencyDAO.deleteAll()
    }
}