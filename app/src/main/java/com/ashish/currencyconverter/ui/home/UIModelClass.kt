package com.ashish.currencyconverter.ui.home

data class UIModelClass(
    var fromCode : String,
    var fromRate : Double,
    var toCode : String,
    var toRate : Double,
    var editableRate : Double
)