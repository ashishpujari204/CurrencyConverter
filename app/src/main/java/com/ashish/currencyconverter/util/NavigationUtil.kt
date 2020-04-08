package com.ashish.currencyconverter.util

import android.app.Activity
import android.content.Intent
import com.ashish.currencyconverter.ui.codelist.CurrencyCodeList
import com.ashish.currencyconverter.ui.home.CurrencyClass
import com.ashish.currencyconverter.ui.home.RateClass

class NavigationUtil {
    companion object {
        fun pickCurrencyCode(
            activity: Activity,
            currencyArrayList: ArrayList<CurrencyClass>,
            rateCodeArray: ArrayList<RateClass>,
            fromCurrencyInputCode: Int
        ) {
            var intent = Intent(activity, CurrencyCodeList::class.java)
            intent.putExtra("CUR_MOCK_ARRAY",currencyArrayList)
            intent.putExtra("CUR_API_ARRAY",rateCodeArray)
            activity.startActivityForResult(intent, fromCurrencyInputCode)
        }

        fun backDataToHomeScreen(
            activity: Activity,
            fromObject: RateClass){
            val result = Intent()
            result.putExtra("OBJECT", fromObject)
            activity.setResult(Activity.RESULT_OK, result)
            activity.finish()
        }
    }
}