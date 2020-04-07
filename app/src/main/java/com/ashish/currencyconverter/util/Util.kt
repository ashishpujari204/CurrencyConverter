package com.ashish.currencyconverter.util

import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class Util {

    companion object {
        fun verifyAvailableNetwork(activity: AppCompatActivity): Boolean {
            val connectivityManager =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        fun getAssetJsonData(context: Context): String? {
            val json: String
            try {
                val inputStream = context.assets.open("currency.json")
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.use { it.read(buffer) }
                json = String(buffer)
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return json
        }
    }

}