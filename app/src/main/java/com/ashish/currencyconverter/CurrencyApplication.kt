package com.ashish.currencyconverter

import android.app.Application
import com.ashish.currencyconverter.di.*
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CurrencyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        startKoin {
            androidContext(this@CurrencyApplication)
            androidLogger(Level.DEBUG)
            modules(listOf(viewModelModule,retrofitModule, roomDatabaseModule,repoImplementation))
        }
    }
}