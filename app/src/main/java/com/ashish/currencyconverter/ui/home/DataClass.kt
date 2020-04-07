package com.ashish.currencyconverter.ui.home
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataClass(
    @SerializedName("base")
    var base: String = "",
    @SerializedName("conversion_rates")
    var conversionRates: ConversionRates = ConversionRates(),
    @SerializedName("documentation")
    var documentation: String = "",
    @SerializedName("result")
    var result: String = "",
    @SerializedName("terms_of_use")
    var termsOfUse: String = "",
    @SerializedName("time_last_update")
    var timeLastUpdate: Int = 0,
    @SerializedName("time_next_update")
    var timeNextUpdate: Int = 0,
    @SerializedName("time_zone")
    var timeZone: String = ""
):Parcelable

@Parcelize
data class ConversionRates(
    @SerializedName("AED")
    var AED: Double = 0.0,
    @SerializedName("ARS")
    var ARS: Double = 0.0,
    @SerializedName("AUD")
    var AUD: Double = 0.0,
    @SerializedName("BGN")
    var BGN: Double = 0.0,
    @SerializedName("BRL")
    var BRL: Double = 0.0,
    @SerializedName("BSD")
    var BSD: Double = 0.0,
    @SerializedName("CAD")
    var CAD: Double = 0.0,
    @SerializedName("CHF")
    var CHF: Double = 0.0,
    @SerializedName("CLP")
    var CLP: Double = 0.0,
    @SerializedName("CNY")
    var CNY: Double = 0.0,
    @SerializedName("COP")
    var COP: Double = 0.0,
    @SerializedName("CZK")
    var CZK: Double = 0.0,
    @SerializedName("DKK")
    var DKK: Double = 0.0,
    @SerializedName("DOP")
    var DOP: Double = 0.0,
    @SerializedName("EGP")
    var EGP: Double = 0.0,
    @SerializedName("EUR")
    var EUR: Double = 0.0,
    @SerializedName("FJD")
    var FJD: Double = 0.0,
    @SerializedName("GBP")
    var GBP: Double = 0.0,
    @SerializedName("GTQ")
    var GTQ: Double = 0.0,
    @SerializedName("HKD")
    var HKD: Double = 0.0,
    @SerializedName("HRK")
    var HRK: Double = 0.0,
    @SerializedName("HUF")
    var HUF: Double = 0.0,
    @SerializedName("IDR")
    var IDR: Double = 0.0,
    @SerializedName("ILS")
    var ILS: Double = 0.0,
    @SerializedName("INR")
    var INR: Double = 0.0,
    @SerializedName("ISK")
    var ISK: Double = 0.0,
    @SerializedName("JPY")
    var JPY: Double = 0.0,
    @SerializedName("KRW")
    var KRW: Double = 0.0,
    @SerializedName("KZT")
    var KZT: Double = 0.0,
    @SerializedName("MXN")
    var MXN: Double = 0.0,
    @SerializedName("MYR")
    var MYR: Double = 0.0,
    @SerializedName("NOK")
    var NOK: Double = 0.0,
    @SerializedName("NZD")
    var NZD: Double = 0.0,
    @SerializedName("PAB")
    var PAB: Double = 0.0,
    @SerializedName("PEN")
    var PEN: Double = 0.0,
    @SerializedName("PHP")
    var PHP: Double = 0.0,
    @SerializedName("PKR")
    var PKR: Double = 0.0,
    @SerializedName("PLN")
    var PLN: Double = 0.0,
    @SerializedName("PYG")
    var PYG: Double = 0.0,
    @SerializedName("RON")
    var RON: Double = 0.0,
    @SerializedName("RUB")
    var RUB: Double = 0.0,
    @SerializedName("SAR")
    var SAR: Double = 0.0,
    @SerializedName("SEK")
    var SEK: Double = 0.0,
    @SerializedName("SGD")
    var SGD: Double = 0.0,
    @SerializedName("THB")
    var THB: Double = 0.0,
    @SerializedName("TRY")
    var TRY: Double = 0.0,
    @SerializedName("TWD")
    var TWD: Double = 0.0,
    @SerializedName("UAH")
    var UAH: Double = 0.0,
    @SerializedName("USD")
    var USD: Int = 0,
    @SerializedName("UYU")
    var UYU: Double = 0.0,
    @SerializedName("ZAR")
    var ZAR: Double = 0.0
):Parcelable