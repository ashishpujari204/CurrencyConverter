package com.ashish.currencyconverter.di

import android.app.Application
import com.ashish.currencyconverter.BuildConfig
import com.ashish.currencyconverter.rest.ApiInterface
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val retrofitModule = module {
    fun provideCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    fun provideHttpClient(cache: Cache): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder().addInterceptor(interceptor).cache(cache)

        return okHttpClientBuilder.build()
    }

    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }


    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
         val key = BuildConfig.API_KEY
         val baseUrl=BuildConfig.BASE_URL+"$key/"
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .addCallAdapterFactory(CoroutineCallAdapterFactory()).client(client).build()
    }
    fun provideCurrencyApi(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }


    single { provideCache(androidApplication()) }
    single { provideHttpClient(get()) }
    single { provideGson() }
    single { provideRetrofit(get(), get()) }
    single { provideCurrencyApi(get()) }
}
private val interceptor: Interceptor
    get() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }


