package com.ashish.currencyconverter.util

import android.app.Activity
import android.content.Intent
import com.ashish.currencyconverter.ui.codelist.CurrencyCodeList
import com.ashish.currencyconverter.ui.home.RateClass

class NavigationUtil {
    companion object {
        fun pickCurrencyCode(activity: Activity,
                             fromCurrencyInputCode: Int) {
            val intent = Intent(activity, CurrencyCodeList::class.java)
            activity.startActivityForResult(intent, fromCurrencyInputCode)
        }

        fun backDataToHomeScreen(activity: Activity, fromObject: RateClass) {
            val result = Intent()
            result.putExtra("OBJECT", fromObject)
            activity.setResult(Activity.RESULT_OK, result)
            activity.finish()
        }
    }
}