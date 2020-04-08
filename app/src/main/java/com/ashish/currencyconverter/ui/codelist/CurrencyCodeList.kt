package com.ashish.currencyconverter.ui.codelist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashish.currencyconverter.R
import com.ashish.currencyconverter.baseclasses.BaseActivity
import com.ashish.currencyconverter.ui.home.CurrencyClass
import com.ashish.currencyconverter.ui.home.RateClass
import com.ashish.currencyconverter.util.NavigationUtil
import kotlinx.android.synthetic.main.activity_currency_code_list.*

class CurrencyCodeList : BaseActivity() {

    lateinit var currencyMockArrayList: ArrayList<CurrencyClass>
    lateinit var rateAPICodeArray: ArrayList<RateClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_code_list)
        initial()
    }

    private fun initial() {
        currencyMockArrayList = intent.getParcelableArrayListExtra("CUR_MOCK_ARRAY")
        rateAPICodeArray = intent.getParcelableArrayListExtra("CUR_API_ARRAY")
        codeList.layoutManager = LinearLayoutManager(this@CurrencyCodeList)
        val codeAdapter =
            CodeListAdapter(currencyMockArrayList)
        codeList.adapter = codeAdapter
        codeList.addItemDecoration(
            DividerItemDecoration(
                this@CurrencyCodeList,
                LinearLayoutManager.VERTICAL
            )
        )
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
}
