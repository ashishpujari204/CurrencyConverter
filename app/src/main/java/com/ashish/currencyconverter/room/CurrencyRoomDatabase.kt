package com.ashish.currencyconverter.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ashish.currencyconverter.ui.home.RateClass

@Database(entities = [RateClass::class], version = 2, exportSchema = false)
 abstract class CurrencyRoomDatabase : RoomDatabase() {
     abstract fun rateDAO(): RateDAO
}