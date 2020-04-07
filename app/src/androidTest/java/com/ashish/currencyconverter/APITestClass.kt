package com.ashish.currencyconverter

import com.ashish.currencyconverter.rest.ApiClient
import com.google.gson.JsonObject
import org.junit.Test
import retrofit2.Call

class APITestClass {
    @Test
    fun getCurrencyData () {
        // call the api
        val dataCall: Call<JsonObject> = ApiClient.getClient.getData("USD")
        val response = dataCall.execute()
        // verify the response is OK
        assert((response.code()) == 200)
    }

}