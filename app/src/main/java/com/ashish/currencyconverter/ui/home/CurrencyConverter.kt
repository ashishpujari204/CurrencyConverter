package com.ashish.currencyconverter.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ashish.currencyconverter.R
import com.ashish.currencyconverter.util.Constants
import com.ashish.currencyconverter.util.NavigationUtil
import com.ashish.currencyconverter.util.Util
import kotlinx.android.synthetic.main.activity_currency_converter.*
import java.lang.NumberFormatException


class CurrencyConverter : AppCompatActivity() {

    lateinit var currencyArrayList: ArrayList<CurrencyClass>
    lateinit var rateCodeArray: ArrayList<RateClass>
    lateinit var currencyViewModel: CurrencyViewModel
    lateinit var tvFromInput: EditText


    val FROM_CURRENCY_INPUT: Int = 1
    val TO_CURRENCY_INPUT: Int = 2

    lateinit var from: String
    lateinit var to: String

    var DEFAULT_FROM_CODE: String = "INR"
    var DEFAULT_TO_CODE: String = "USD"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)
        currencyArrayList = ArrayList()
        rateCodeArray = ArrayList()
        currencyViewModel =
            ViewModelProviders.of(this@CurrencyConverter).get(CurrencyViewModel::class.java)
        tvFromInput = findViewById(R.id.tvFromInput)
        loadMockCurrencyCode()
        from = Constants.getFromCode(this@CurrencyConverter)
        to = Constants.getToCode(this@CurrencyConverter)

        initial()

    }

    private fun initial() {
        if (Util.verifyAvailableNetwork(this@CurrencyConverter)) {
            if (from == "NA" && to == "NA") {
                Constants.saveFromCode(this@CurrencyConverter, DEFAULT_FROM_CODE)
                Constants.saveToCode(this@CurrencyConverter, DEFAULT_TO_CODE)
                getData(DEFAULT_FROM_CODE, DEFAULT_TO_CODE, false)
            } else {
                getData(from, to, false)
            }
        } else {
            currencyViewModel.newRecords.observe(this, Observer {
                rateCodeArray.addAll(it)
                if (rateCodeArray.isNotEmpty()) {
                    if (from == "NA" && to == "NA") {
                        Constants.saveFromCode(this@CurrencyConverter, DEFAULT_FROM_CODE)
                        Constants.saveToCode(this@CurrencyConverter, DEFAULT_TO_CODE)
                        setDefaultDataToView(DEFAULT_FROM_CODE, DEFAULT_TO_CODE)
                    } else {
                        setDefaultDataToView(from, to)
                    }
                } else {
                    showToast(resources.getString(R.string.network_connection))
                }
            })

        }


        currencySwipeButton.setOnClickListener {
            if (Util.verifyAvailableNetwork(this@CurrencyConverter)) {
                Constants.saveFromCode(this@CurrencyConverter, tvToCode.text.toString())
                Constants.saveToCode(this@CurrencyConverter, tvFromCode.text.toString())
                getData(tvToCode.text.toString(), tvFromCode.text.toString(), true)
            } else {
                showToast(resources.getString(R.string.network_connection))
            }
        }

        setupCalculation()
        setupNavigation()
    }

    private fun setupCalculation() {
        tvFromInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                try {
                    if (s != null && s.isNotEmpty() && rateCodeArray.isNotEmpty()) {
                        var toObject = getToCurrencyObject();
                        var calculatedAmount = s.toString().toDouble() * toObject?.rate!!
                        tvToInput.text = "" + Util.roundOffDecimal(calculatedAmount)
                    }
                } catch (e: NumberFormatException) {
                    logError(e.toString())
                }

            }
        })
    }

    private fun logError(toString: String) {
        Log.e("CurrencyConverter",toString)
    }

    private fun setupNavigation() {
        lyFromCurrencySelection.setOnClickListener {
            if (Util.verifyAvailableNetwork(this@CurrencyConverter)) {
                NavigationUtil.pickCurrencyCode(this@CurrencyConverter,
                    currencyArrayList,
                    rateCodeArray,
                    FROM_CURRENCY_INPUT)
            } else {
                showToast(resources.getString(R.string.network_connection))
            }
        }
        lyToCurrencySelection.setOnClickListener {
            NavigationUtil.pickCurrencyCode(this@CurrencyConverter,
                currencyArrayList,
                rateCodeArray,
                TO_CURRENCY_INPUT)
        }
    }

    private fun getData(base: String, toCode: String, needToUpdateArray: Boolean) {
        progressBar.visibility= View.VISIBLE
        currencyViewModel.getCurrencyData(base).observe(this@CurrencyConverter, Observer {
            progressBar.visibility= View.GONE
            parseData(it, base, toCode, needToUpdateArray)
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
    private fun parseData(response: String,
                          fromCode: String,
                          toCode: String,
                          needToUpdateArray: Boolean) {

        if (rateCodeArray.isNotEmpty()) {
            rateCodeArray.clear()
        }
        /*if(needToUpdateArray) {
            currencyViewModel.newRecords.observe(this, Observer {
                rateCodeArray.addAll(it)
                logError("loop activity records data--$rateCodeArray")
                setDefaultDataToView(fromCode, toCode)
            })
        }*/
        if (rateCodeArray.isEmpty()) {
            updateDataFromService(response, fromCode, toCode)
        }

    }

    /**
     * sometimes insert take time for that we will take data from server
     */
    private fun updateDataFromService(response: String, fromCode: String, toCode: String) {
        rateCodeArray.addAll(currencyViewModel.parseJson(response))
        currencyViewModel.insert(rateCodeArray)
        setDefaultDataToView(fromCode, toCode)
    }

    private fun getToCurrencyObject(): RateClass? {
        return rateCodeArray.find { it.code == tvToCode.text }
    }


    /**
     * parsing mock country and code data here for show purpose
     */
    private fun loadMockCurrencyCode() {
        currencyArrayList = currencyViewModel.getMockCountryCode(this@CurrencyConverter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FROM_CURRENCY_INPUT) {
            if (resultCode == Activity.RESULT_OK) {
                val rate = data?.getParcelableExtra<RateClass>("OBJECT")
                rate?.let {
                    Constants.saveFromCode(this@CurrencyConverter, rate.code)
                    getData(rate.code, tvToCode.text.toString(), true)

                }
            }
        }
        if (requestCode == TO_CURRENCY_INPUT) {
            if (resultCode == Activity.RESULT_OK) {
                val rate = data?.getParcelableExtra<RateClass>("OBJECT")
                rate?.let {
                    Constants.saveToCode(this@CurrencyConverter, rate.code)
                    setDefaultDataToView(tvFromCode.text.toString(), rate.code)
                }
            }
        }
    }
    private fun showToast(string: String) {
        Toast.makeText(this@CurrencyConverter,string, Toast.LENGTH_SHORT).show()
    }
}
