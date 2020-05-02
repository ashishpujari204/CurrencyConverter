package com.ashish.currencyconverter.di

import android.app.Application
import androidx.room.Room
import com.ashish.currencyconverter.rest.ApiInterface
import com.ashish.currencyconverter.rest.RepositoryImplementation
import com.ashish.currencyconverter.room.CurrencyRoomDatabase
import com.ashish.currencyconverter.room.RateDAO
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomDatabaseModule = module {

    fun provideDatabase(application: Application): CurrencyRoomDatabase {
        return Room.databaseBuilder(application,
                CurrencyRoomDatabase::class.java,
                "currency_database.database").fallbackToDestructiveMigration()
            .allowMainThreadQueries().build()
    }


    fun provideDao(database: CurrencyRoomDatabase): RateDAO {
        return database.rateDAO()
    }

    val repositoryModule = module {
        fun provideCurrencyRepo(apiInterface: ApiInterface,
                                rateDAO: RateDAO,application: Application): RepositoryImplementation {
            return RepositoryImplementation(apiInterface, rateDAO, application)
        }

        single { provideCurrencyRepo(get(), get(),androidApplication()) }
    }
    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
    single { repositoryModule }
}