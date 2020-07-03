package com.ashish.currencyconverter.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.ashish.currencyconverter.util.Constants


class SharedPreferencesStorage(context: Context) : Storage {

    var masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private var sharedPreferences: SharedPreferences =
        EncryptedSharedPreferences.create(Constants.PREFERANCE_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

    override fun setString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun getString(key: String): String {
        return sharedPreferences.getString(key, "NA")!!
    }
}
