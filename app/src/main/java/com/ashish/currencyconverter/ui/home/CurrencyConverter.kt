package com.ashish.currencyconverter.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ashish.currencyconverter.R
import com.ashish.currencyconverter.baseclasses.BaseActivity
import com.ashish.currencyconverter.util.Constants.Companion.CONVERSATION_RATES
import com.ashish.currencyconverter.util.Constants.Companion.DEFAULT_VALUE
import com.ashish.currencyconverter.util.Constants.Companion.RESULT
import com.ashish.currencyconverter.util.Constants.Companion.SUCCESS
import kotlinx.android.synthetic.main.activity_currency_converter.*
import org.json.JSONObject

class CurrencyConverter : BaseActivity() {

    lateinit var currencyArrayList : ArrayList<CurrencyClass>
    lateinit var rateCodeArray : ArrayList<RateClass>
    lateinit var currencyViewModel: CurrencyViewModel
    lateinit var tvFromInput : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)
        currencyViewModel= ViewModelProviders.of(this@CurrencyConverter).get(CurrencyViewModel::class.java)
        tvFromInput = findViewById(R.id.tvFromInput)
        loadCurrencyCode()
        getData("INR","USD")

        tvFromInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                if(s.isNotEmpty()){
                    var calculatedAmount = s.toString().toDouble() * getToCurrencyObject()?.rate!!
                    tvToInput.text=""+calculatedAmount
                }
            }
        })
        currencySwipeButton.setOnClickListener {
            //setDefaultDataToView(tvToCode.text.toString(),tvFromCode.text.toString())
            getData(tvToCode.text.toString(),tvFromCode.text.toString())
        }
    }

    private fun getData(base:String,toCode : String) {
        showProgressDialog()
        rateCodeArray= ArrayList()
        currencyViewModel.getCurrencyData(base).observe(this@CurrencyConverter, Observer {
            stopProgressDialog()
            if(it!=null){
               parseData(it,base,toCode)
            }else{
                showToast(resources.getString(R.string.error_message))
            }
        })

    }

    private fun setDefaultDataToView(fromCode : String , toCode : String) {
        var fromObject=rateCodeArray.find { it.code==fromCode }
        var toObject=rateCodeArray.find { it.code==toCode }
        tvFromCode.text=fromObject?.code
        tvToRate.text=""+fromObject?.rate+" "+fromObject?.code

        tvToCode.text=toObject?.code
        tvFromRate.text=""+toObject?.rate+" "+toObject?.code

        tvFromInput.setText(""+fromObject?.rate)
        tvToInput.text = ""+toObject?.rate

        tvFromInputCode.text=fromObject?.code
        tvToInputCode.text=toObject?.code
    }

    /**
     * parse api response
     */
    private fun parseData(it: String,fromCode : String, toCode: String) {
        logError(it)
        var jsonObject=JSONObject(it)
        if(jsonObject.optString(RESULT,DEFAULT_VALUE)==SUCCESS){
            var rateObject=jsonObject.getJSONObject(CONVERSATION_RATES)
            var keys=rateObject.keys()
            while(keys.hasNext()){
                var rateCodeObject=RateClass()
                var keyValue=keys.next()
                rateCodeObject.code=keyValue
                rateCodeObject.rate=rateObject.optDouble(keyValue,0.0)
                rateCodeArray.add(rateCodeObject)
            }
        }
        setDefaultDataToView(fromCode,toCode)
    }

    private fun getToCurrencyObject() : RateClass?
    {
        return rateCodeArray.find { it.code==tvToCode.text }
    }

    private fun getFromCurrencyObject() : RateClass?
    {
        return rateCodeArray.find { it.code==tvFromCode.text }
    }

    /**
     * parsing mock country and code data here for show purpose
     */
    private fun loadCurrencyCode() {
        currencyArrayList= currencyViewModel.getMockCountryCode(this@CurrencyConverter)
    }
}
