package com.ashish.currencyconverter.ui.home
import com.google.gson.annotations.SerializedName


data class CurrencyClass(
    @SerializedName("code")
    var code: String = "",
    @SerializedName("decimal_digits")
    var decimalDigits: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("name_plural")
    var namePlural: String = "",
    @SerializedName("rounding")
    var rounding: Int = 0,
    @SerializedName("symbol")
    var symbol: String = "",
    @SerializedName("symbol_native")
    var symbolNative: String = ""
)