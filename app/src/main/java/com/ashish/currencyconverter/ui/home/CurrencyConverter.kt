package com.ashish.currencyconverter.ui.home

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ashish.currencyconverter.R
import com.ashish.currencyconverter.baseclasses.BaseActivity
import com.ashish.currencyconverter.util.Constants.Companion.SUCCESS
import com.ashish.currencyconverter.util.Util
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_currency_converter.*
import org.json.JSONArray
import org.json.JSONObject

class CurrencyConverter : BaseActivity() {

    var DEFAULT_VALUE="NA"
    lateinit var currencyArrayList : ArrayList<CurrencyClass>
    lateinit var rateCodeArray : ArrayList<RateClass>
    lateinit var currencyViewModel: CurrencyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)
        currencyViewModel= ViewModelProviders.of(this@CurrencyConverter).get(CurrencyViewModel::class.java)
        loadCurrencyCode()
        getData("USD")

    }

    private fun getData(base:String) {
        showProgressDialog()
        rateCodeArray= ArrayList()
        currencyViewModel.getCurrencyData(base).observe(this@CurrencyConverter, Observer {
            stopProgressDialog()
            if(it!=null){
               parseData(it)
            }else{
                showToast(resources.getString(R.string.error_message))
            }
        })
        setDefaultDataToView()
    }

    private fun setDefaultDataToView() {
        var inrObject=rateCodeArray.find { it.code=="INR" }
        var usdObject=rateCodeArray.find { it.code=="USD" }
        tvFromCode.text=inrObject?.code
        tvToRate.text=""+inrObject?.rate+" "+inrObject?.code

        tvToCode.text=usdObject?.code
        tvFromRate.text=""+usdObject?.rate+" "+usdObject?.code
    }

    /**
     * parse api response
     */
    private fun parseData(it: JsonObject) {
        var jsonObject=JSONObject(it.toString())
        if(jsonObject.optString("result",DEFAULT_VALUE)==SUCCESS){
            var rateObject=jsonObject.getJSONObject("conversion_rates")
            var keys=rateObject.keys()
            while(keys.hasNext()){
                var rateCodeObject=RateClass()
                var keyValue=keys.next()
                rateCodeObject.code=keyValue
                rateCodeObject.rate=rateObject.optDouble(keyValue,0.0)
                rateCodeArray.add(rateCodeObject)
            }
        }
    }

    /**
     * parsing mock country and code data here for show purpose
     */
    private fun loadCurrencyCode() {
        currencyArrayList= ArrayList();
        var currencyArray= JSONArray(Util.getAssetJsonData(this@CurrencyConverter))
        for (i in 0 until currencyArray.length()) {
            val currencyObjects = currencyArray.getJSONObject(i)
            var keys=currencyObjects.keys()
            while(keys.hasNext()){
                var key=keys.next()
                var currencyObject=currencyObjects.getJSONObject(key)
                var currencyModel=CurrencyClass()
                currencyModel.name=currencyObject.optString("name",DEFAULT_VALUE)
                currencyModel.symbol=currencyObject.optString("symbol",DEFAULT_VALUE)
                currencyModel.symbolNative=currencyObject.optString("symbol_native",DEFAULT_VALUE)
                currencyModel.code=currencyObject.optString("code",DEFAULT_VALUE)
                currencyModel.namePlural=currencyObject.optString("name_plural",DEFAULT_VALUE)
                currencyArrayList.add(currencyModel)
            }
        }
    }
}
