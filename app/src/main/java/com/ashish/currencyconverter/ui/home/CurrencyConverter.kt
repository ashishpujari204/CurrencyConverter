package com.ashish.currencyconverter.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ashish.currencyconverter.R
import com.ashish.currencyconverter.databinding.ActivityCurrencyConverterBinding
import com.ashish.currencyconverter.util.Constants
import com.ashish.currencyconverter.util.Constants.Companion.DEFAULT_FROM_CODE
import com.ashish.currencyconverter.util.Constants.Companion.DEFAULT_TO_CODE
import com.ashish.currencyconverter.util.Constants.Companion.FROM_CURRENCY_INPUT
import com.ashish.currencyconverter.util.Constants.Companion.TO_CURRENCY_INPUT
import com.ashish.currencyconverter.util.Util
import kotlinx.android.synthetic.main.activity_currency_converter.*


class CurrencyConverter : AppCompatActivity() {

    lateinit var currencyArrayList: ArrayList<CurrencyClass>
    lateinit var rateCodeArray: ArrayList<RateClass>
    lateinit var currencyViewModel: CurrencyViewModel
    lateinit var tvFromInput: EditText
    lateinit var from: String
    lateinit var to: String

    lateinit var activityCurrencyConverterBinding: ActivityCurrencyConverterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCurrencyConverterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_currency_converter)
        currencyViewModel =
            ViewModelProvider(this@CurrencyConverter).get(CurrencyViewModel::class.java)
        activityCurrencyConverterBinding.viewModel = currencyViewModel
        currencyArrayList = ArrayList()
        rateCodeArray = ArrayList()

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

    }

    private fun getData(base: String, toCode: String, needToUpdateArray: Boolean) {
        progressBar.visibility = View.VISIBLE
        currencyViewModel.getCurrencyData(base,applicationContext).observe(this@CurrencyConverter, Observer {
            progressBar.visibility = View.GONE
            parseData(it, base, toCode, needToUpdateArray)
        })

    }

    private fun setDefaultDataToView(fromCode: String, toCode: String) {
        var fromObject = rateCodeArray.find { it.code == fromCode }
        var toObject = rateCodeArray.find { it.code == toCode }

        var uiModelClass =
            UIModelClass(fromObject!!.code, fromObject.rate, toObject!!.code, toObject.rate,fromObject.rate)
        currencyViewModel.fromInputText.set(toObject.rate)
        currencyViewModel.uiModelClassObj.set(uiModelClass)
        activityCurrencyConverterBinding.uiClassObject = uiModelClass
        tvToCode.text = toObject?.code
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
        currencyViewModel.insert(currencyViewModel.parseJson(response))
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
        Toast.makeText(this@CurrencyConverter, string, Toast.LENGTH_SHORT).show()
    }




}
