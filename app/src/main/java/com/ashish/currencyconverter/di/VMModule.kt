package com.ashish.currencyconverter.di

import com.ashish.currencyconverter.rest.RepositoryImplementation
import com.ashish.currencyconverter.ui.home.CurrencyViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val viewModelModule = module {
    factory { CurrencyViewModel(get(),get()) }
}
val repoImplementation = module {
    factory { RepositoryImplementation(get(),get(),androidApplication()) }
}
