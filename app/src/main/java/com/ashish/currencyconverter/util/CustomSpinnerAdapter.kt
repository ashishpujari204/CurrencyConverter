package com.ashish.currencyconverter.util

import android.app.Activity
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ashish.currencyconverter.R
import com.ashish.currencyconverter.ui.home.CurrencyClass
import com.ashish.currencyconverter.ui.home.RateClass
import java.util.*


class CustomSpinnerAdapter(private val activity: Activity, private val asr: ArrayList<RateClass>) :
    BaseAdapter(), SpinnerAdapter {


    override fun getCount(): Int {
        return asr.size
    }

    override fun getItem(i: Int): Any {
        return asr[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val txt = TextView(activity)
        txt.setPadding(16, 16, 16, 16)
        txt.gravity = Gravity.CENTER_VERTICAL
        val face = Typeface.createFromAsset(
            activity.assets,
            "Fonts/google_sans_regular.ttf"
        )
        txt.setTypeface(face)
        txt.text = asr[position].code
        txt.setTextAppearance(activity, android.R.style.TextAppearance_Medium)
        txt.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimaryDark))
        return txt
    }

    override fun getView(
        i: Int,
        view: View?,
        viewgroup: ViewGroup
    ): View {
        val txt = TextView(activity)
        txt.gravity = Gravity.CENTER
        txt.setPadding(16, 16, 16, 16)
        val face = Typeface.createFromAsset(
            activity.assets,
            "Fonts/google_sans_regular.ttf"
        )
        txt.setTypeface(face)
        txt.setTextAppearance(activity, android.R.style.TextAppearance_Medium)
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_ic, 0)
        txt.text = asr[i].code
        txt.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimaryDark))
        return txt
    }

}