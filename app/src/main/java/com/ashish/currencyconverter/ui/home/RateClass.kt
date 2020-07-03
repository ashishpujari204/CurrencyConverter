package com.ashish.currencyconverter.ui.home

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "currency")
data class RateClass (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "code")
    var code: String ,
    @ColumnInfo(name = "rate")
    var rate:Double

):Parcelable