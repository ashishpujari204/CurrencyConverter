package com.ashish.currencyconverter.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ashish.currencyconverter.ui.home.RateClass

@Dao
interface RateDAO {

    @Query("SELECT * from currency")
    fun getCodes(): List<RateClass>

    @Query("SELECT * from currency")
    fun getLiveRecords(): LiveData<List<RateClass>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insert(list: List<RateClass>)

    @Query("DELETE FROM currency")
    suspend fun deleteAll()

    @Query("SELECT count(*) from currency")
    fun getRowCount(): Int
}