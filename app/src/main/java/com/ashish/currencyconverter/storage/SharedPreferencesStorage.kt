package com.ashish.currencyconverter.storage


import android.content.Context
import com.app.smartprocessors.storage.Storage
import com.ashish.currencyconverter.util.Constants

class SharedPreferencesStorage(context: Context) : Storage {

    private val sharedPreferences = context.getSharedPreferences(Constants.PREFERANCE_NAME, Context.MODE_PRIVATE)

    override fun setString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun getString(key: String): String {
        return sharedPreferences.getString(key, "")!!
    }
}
