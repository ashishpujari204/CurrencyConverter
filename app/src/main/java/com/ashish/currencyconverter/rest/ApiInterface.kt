package com.ashish.currencyconverter.rest


import com.ashish.currencyconverter.ui.home.DataClass
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @GET("latest/{base}")
    fun getData(
        @Path("base") base: String?
    ): Call<JsonObject>

    /* @GET("latest")
     fun getData(
         @Query("base") base: String
     ): Call<JsonObject>*/


}