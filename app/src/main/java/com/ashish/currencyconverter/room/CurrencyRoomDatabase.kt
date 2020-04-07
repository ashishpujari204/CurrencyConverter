package com.ashish.currencyconverter.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ashish.currencyconverter.ui.home.RateClass

@Database(entities = [RateClass::class], version = 1, exportSchema = false)
public abstract class CurrencyRoomDatabase : RoomDatabase() {

    abstract fun rateDAO(): RateDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CurrencyRoomDatabase? = null

        fun getDatabase(context: Context): CurrencyRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CurrencyRoomDatabase::class.java,
                    "currency_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}