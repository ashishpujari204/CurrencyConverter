package com.ashish.currencyconverter

import android.app.Application
import com.facebook.stetho.Stetho

class CurrencyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}