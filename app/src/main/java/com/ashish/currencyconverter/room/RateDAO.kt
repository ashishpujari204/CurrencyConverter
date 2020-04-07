package com.ashish.currencyconverter.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ashish.currencyconverter.ui.home.RateClass

@Dao
interface RateDAO {

    @Query("SELECT * from currency ORDER BY code ASC")
    fun getCodes(): List<RateClass>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCode(rateObject: RateClass)

    @Query("DELETE FROM currency")
    suspend fun deleteAll()

}