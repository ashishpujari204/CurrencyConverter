package com.ashish.currencyconverter.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelProviders.*
import com.ashish.currencyconverter.R
import com.ashish.currencyconverter.baseclasses.BaseActivity
import com.ashish.currencyconverter.ui.codelist.CurrencyCodeList
import com.ashish.currencyconverter.util.Constants
import com.ashish.currencyconverter.util.Constants.Companion.CONVERSATION_RATES
import com.ashish.currencyconverter.util.Constants.Companion.DEFAULT_VALUE
import com.ashish.currencyconverter.util.Constants.Companion.RESULT
import com.ashish.currencyconverter.util.Constants.Companion.SUCCESS
import com.ashish.currencyconverter.util.NavigationUtil
import com.ashish.currencyconverter.util.Util
import kotlinx.android.synthetic.main.activity_currency_converter.*
import org.json.JSONObject
import java.lang.NullPointerException
import java.lang.NumberFormatException


class CurrencyConverter : BaseActivity() {

    lateinit var currencyArrayList: ArrayList<CurrencyClass>
    lateinit var rateCodeArray: ArrayList<RateClass>
    lateinit var currencyViewModel: CurrencyViewModel
    lateinit var tvFromInput: EditText

    val FROM_CURRENCY_INPUT: Int = 1
    val TO_CURRENCY_INPUT: Int = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)
        currencyArrayList = ArrayList()
        rateCodeArray = ArrayList()
        currencyViewModel = ViewModelProviders.of(this@CurrencyConverter).get(CurrencyViewModel::class.java)
        tvFromInput = findViewById(R.id.tvFromInput)
        loadCurrencyCode()
        var from=Constants.getFromCode(this@CurrencyConverter)
        var to=Constants.getToCode(this@CurrencyConverter)

        if (Util.verifyAvailableNetwork(this@CurrencyConverter)) {
            if(from=="NA" && to=="NA") {
                Constants.saveFromCode(this@CurrencyConverter,"INR")
                Constants.saveToCode(this@CurrencyConverter,"USD")
                getData("INR", "USD")
            }else{
                getData(from, to)
            }
        } else {
            rateCodeArray.addAll(currencyViewModel.getCode())
            getData(from, to)
        }


        currencySwipeButton.setOnClickListener {
            getData(tvToCode.text.toString(), tvFromCode.text.toString())
            Constants.saveFromCode(this@CurrencyConverter,tvToCode.text.toString())
            Constants.saveToCode(this@CurrencyConverter,tvFromCode.text.toString())
        }
        setupCalculation()
        setupNavigation()
    }

    private fun setupCalculation() {
        tvFromInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                try {
                    if (s != null && s.isNotEmpty() && rateCodeArray.isNotEmpty()) {
                        var calculatedAmount =
                            s.toString().toDouble() * getToCurrencyObject()?.rate!!
                        tvToInput.text = "" + Util.roundOffDecimal(calculatedAmount)
                    }
                }catch (e : NumberFormatException){
                    logError(e.toString())
                }
            }
        })
    }

    private fun setupNavigation() {
        lyFromCurrencySelection.setOnClickListener {
            if (Util.verifyAvailableNetwork(this@CurrencyConverter)) {
                NavigationUtil.pickCurrencyCode(
                    this@CurrencyConverter,
                    currencyArrayList,
                    rateCodeArray,
                    FROM_CURRENCY_INPUT
                )
            }else{
                showToast(resources.getString(R.string.network_connection))
            }
        }
        lyToCurrencySelection.setOnClickListener {
            NavigationUtil.pickCurrencyCode(
                this@CurrencyConverter,
                currencyArrayList,
                rateCodeArray,
                TO_CURRENCY_INPUT
            )
        }
    }

    private fun getData(base: String, toCode: String) {
        showProgressDialog()
        rateCodeArray = ArrayList()
        currencyViewModel.getCurrencyData(base).observe(this@CurrencyConverter, Observer {
            stopProgressDialog()
            if (it != null) {
                parseData(it, base, toCode)
            } else {
                showToast(resources.getString(R.string.error_message))
            }
        })

    }

    private fun setDefaultDataToView(fromCode: String, toCode: String) {
        var fromObject = rateCodeArray.find { it.code == fromCode }
        var toObject = rateCodeArray.find { it.code == toCode }
        tvFromCode.text = fromObject?.code
        tvToRate.text = "" + fromObject?.rate + " " + fromObject?.code

        tvToCode.text = toObject?.code
        tvFromRate.text = "" + toObject?.rate + " " + toObject?.code

        tvFromInput.setText("" + fromObject?.rate)
        tvToInput.text = "" + toObject?.rate

        tvFromInputCode.text = fromObject?.code
        tvToInputCode.text = toObject?.code
    }

    /**
     * parse api response
     */
    private fun parseData(it: String, fromCode: String, toCode: String) {

        var jsonObject = JSONObject(it)
        if (jsonObject.optString(RESULT, DEFAULT_VALUE) == SUCCESS) {
            currencyViewModel.delete()

            var rateObject = jsonObject.getJSONObject(CONVERSATION_RATES)
            var keys = rateObject.keys()

            while (keys.hasNext()) {
                var keyValue = keys.next()
                var rateCodeObject = RateClass(0, keyValue, rateObject.optDouble(keyValue, 0.0))
                //currencyViewModel.insert(rateCodeObject)
                rateCodeArray.add(rateCodeObject)
            }
        }

       // rateCodeArray.addAll(currencyViewModel.getCode())

        setDefaultDataToView(fromCode, toCode)
    }

    private fun getToCurrencyObject(): RateClass? {
        return rateCodeArray.find { it.code == tvToCode.text }
    }

    private fun getFromCurrencyObject(): RateClass? {
        return rateCodeArray.find { it.code == tvFromCode.text }
    }

    /**
     * parsing mock country and code data here for show purpose
     */
    private fun loadCurrencyCode() {
        currencyArrayList = currencyViewModel.getMockCountryCode(this@CurrencyConverter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FROM_CURRENCY_INPUT) {
            if (resultCode == Activity.RESULT_OK) {
                val rate = data?.getParcelableExtra<RateClass>("OBJECT")
                rate?.let {
                    Constants.saveFromCode(this@CurrencyConverter,rate.code)
                    getData(rate.code, tvToCode.text.toString())

                }
            }
        }
        if (requestCode == TO_CURRENCY_INPUT) {
            if (resultCode == Activity.RESULT_OK) {
                val rate = data?.getParcelableExtra<RateClass>("OBJECT")
                rate?.let {
                    Constants.saveToCode(this@CurrencyConverter,rate.code)
                    setDefaultDataToView(tvFromCode.text.toString(), rate.code)
                }
            }
        }
    }
}
