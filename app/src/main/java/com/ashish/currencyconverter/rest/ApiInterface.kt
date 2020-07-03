package com.ashish.currencyconverter.rest


import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET("latest/{base}")
     fun getDataAsync(
        @Path("base") base: String?
    ): Deferred<Response<JsonObject>>

}