package com.ashish.currencyconverter.util

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatEditText


class CustomEditText : AppCompatEditText {

    /**
     * custom views
     * @param context
     */
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        this.isInEditMode
        val tf = Typeface.createFromAsset(context.assets, Constants.font_name)
        typeface = tf
    }

}
