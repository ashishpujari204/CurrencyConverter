package com.ashish.currencyconverter.util

import android.app.Activity
import com.ashish.currencyconverter.storage.SharedPreferencesStorage

class Constants {

    companion object {

        const val font_name = "Fonts/google_sans_regular.ttf"
        const val PERFERENCES_NAME = "currency_convertor"

        const val DEFAULT_VALUE = "NA"

        /**
         * Json key
         */

        const val SUCCESS = "success"
        const val RESULT = "result"
        const val CONVERSATION_RATES = "conversion_rates"

        private const val FROM_CODE = "from_code"
        private const val TO_CODE = "to_code"

        const val DEFAULT_FROM_CODE: String = "INR"
        const val DEFAULT_TO_CODE: String = "USD"
        const val FROM_CURRENCY_INPUT: Int = 1
        const val TO_CURRENCY_INPUT: Int = 2

        fun saveFromCode(activity: Activity, fromCode: String) {
            SharedPreferencesStorage(activity).setString(FROM_CODE, fromCode)
        }

        fun saveToCode(activity: Activity, toCode: String) {
            SharedPreferencesStorage(activity).setString(TO_CODE, toCode)
        }

        fun getFromCode(activity: Activity): String {
            return SharedPreferencesStorage(activity).getString(FROM_CODE)
        }

        fun getToCode(activity: Activity): String {
            return SharedPreferencesStorage(activity).getString(TO_CODE)
        }

    }

}