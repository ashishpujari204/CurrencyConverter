package com.ashish.currencyconverter.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import com.ashish.currencyconverter.ui.home.CurrencyClass
import com.ashish.currencyconverter.util.Constants.Companion.DEFAULT_VALUE
import org.json.JSONArray
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat

class Util {

    companion object {
        fun verifyAvailableNetwork(activity: AppCompatActivity): Boolean {
            val connectivityManager =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        private fun getAssetJsonData(context: Context): String? {
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

        fun roundOffDecimal(number: Double): Double {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }
        fun getMockCountryCode(activity: Activity): ArrayList<CurrencyClass> {
            var currencyArray = JSONArray(getAssetJsonData(activity))
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

}