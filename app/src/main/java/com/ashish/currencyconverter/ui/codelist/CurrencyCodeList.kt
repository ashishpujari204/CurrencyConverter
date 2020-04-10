package com.ashish.currencyconverter.ui.codelist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashish.currencyconverter.R
import com.ashish.currencyconverter.databinding.ActivityCurrencyCodeListBinding
import com.ashish.currencyconverter.ui.home.CurrencyClass
import com.ashish.currencyconverter.ui.home.RateClass
import com.ashish.currencyconverter.util.NavigationUtil
import com.ashish.currencyconverter.util.Util
import kotlinx.android.synthetic.main.activity_currency_code_list.*

class CurrencyCodeList : AppCompatActivity() {

    lateinit var currencyMockArrayList: ArrayList<CurrencyClass>
    lateinit var rateAPICodeArray: ArrayList<RateClass>
    lateinit var codeAdapter: CodeListAdapter
    lateinit var currencyCodeListBinding: ActivityCurrencyCodeListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currencyCodeListBinding=DataBindingUtil.setContentView(this,R.layout.activity_currency_code_list)
        initial()
    }

    private fun initial() {
        currencyMockArrayList = Util.getMockCountryCode(this@CurrencyCodeList)
        rateAPICodeArray = intent.getParcelableArrayListExtra("CUR_API_ARRAY")
        setDataToAdapter()
        clickEvent()
    }
    private fun setDataToAdapter() {
        codeAdapter = CodeListAdapter(currencyMockArrayList)
        currencyCodeListBinding.codeList.adapter = codeAdapter
        currencyCodeListBinding.codeList.addItemDecoration(DividerItemDecoration(this@CurrencyCodeList,
            LinearLayoutManager.VERTICAL))
    }
    private fun clickEvent() {
        codeAdapter.setClickListener(object : CodeListAdapter.ClickListener {
            override fun onItemClick(v: View, position: Int) {
                var fromObject =
                    rateAPICodeArray.find { it.code == currencyMockArrayList[position].code }
                if (fromObject != null) {
                    NavigationUtil.backDataToHomeScreen(this@CurrencyCodeList, fromObject)
                } else {
                    showToast(resources.getString(R.string.selection_message))
                }
            }
        })
        backToHome.setOnClickListener { finish() }
    }

    private fun showToast(string: String) {
        Toast.makeText(this@CurrencyCodeList,string,Toast.LENGTH_SHORT).show()
    }


}
