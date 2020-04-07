package com.ashish.currencyconverter.ui.home

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RateClass (
    var code: String = "",
    var rate:Double=0.0

):Parcelable