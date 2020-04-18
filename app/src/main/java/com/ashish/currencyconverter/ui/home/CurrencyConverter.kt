package com.ashish.currencyconverter.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.ashish.currencyconverter.R
import com.ashish.currencyconverter.databinding.ActivityCurrencyConverterBinding
import com.ashish.currencyconverter.util.Constants
import com.ashish.currencyconverter.util.Constants.Companion.DEFAULT_FROM_CODE
import com.ashish.currencyconverter.util.Constants.Companion.DEFAULT_TO_CODE
import com.ashish.currencyconverter.util.Constants.Companion.FROM_CURRENCY_INPUT
import com.ashish.currencyconverter.util.Constants.Companion.TO_CURRENCY_INPUT
import com.ashish.currencyconverter.util.Util
import kotlinx.android.synthetic.main.activity_currency_converter.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CurrencyConverter : AppCompatActivity() {

    private val currencyViewModel by viewModel<CurrencyViewModel>()

    lateinit var from: String
    lateinit var to: String

    lateinit var currencyArrayList: ArrayList<CurrencyClass>
    lateinit var rateCodeArray: ArrayList<RateClass>

    lateinit var activityCurrencyConverterBinding: ActivityCurrencyConverterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCurrencyConverterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_currency_converter)
        activityCurrencyConverterBinding.viewModel = currencyViewModel
        currencyArrayList = ArrayList()
        rateCodeArray = ArrayList()

        /**
         * getting stored from and to currency code from shared preference
         */
        from = Constants.getFromCode(this@CurrencyConverter)
        to = Constants.getToCode(this@CurrencyConverter)

        initial()

    }

    /**
     * checking network calls and setup click event.
     */
    private fun initial() {
        if (Util.verifyAvailableNetwork(this@CurrencyConverter)) {
            if (from == "NA" && to == "NA") {
                Constants.saveFromCode(this@CurrencyConverter, DEFAULT_FROM_CODE)
                Constants.saveToCode(this@CurrencyConverter, DEFAULT_TO_CODE)
                getData(DEFAULT_FROM_CODE, DEFAULT_TO_CODE)
            } else {
                getData(from, to)
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
                getData(tvToCode.text.toString(), tvFromCode.text.toString())
            } else {
                showToast(resources.getString(R.string.network_connection))
            }
        }

    }

    /**
     * call api from view model and called parse data method.
     */
    private fun getData(base: String, toCode: String) {
        progressBar.visibility = View.VISIBLE
        currencyViewModel.getCurrencyData(base,applicationContext).observe(this@CurrencyConverter, Observer {
            progressBar.visibility = View.GONE
            parseData(it, base, toCode)
        })

    }

    /**
     * set default result to ui and update data binding data
     */
    private fun setDefaultDataToView(fromCode: String, toCode: String) {
        val fromObject = rateCodeArray.find { it.code == fromCode }
        val toObject = rateCodeArray.find { it.code == toCode }

        val uiModelClass =
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
                          toCode: String) {

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


    /**
     * get selected currency code from list and update ui
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FROM_CURRENCY_INPUT) {
            if (resultCode == Activity.RESULT_OK) {
                val rate = data?.getParcelableExtra<RateClass>("OBJECT")
                rate?.let {
                    Constants.saveFromCode(this@CurrencyConverter, rate.code)
                    getData(rate.code, tvToCode.text.toString())

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
