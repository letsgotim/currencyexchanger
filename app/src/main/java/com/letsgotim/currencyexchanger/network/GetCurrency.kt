package com.letsgotim.currencyexchanger.network


import com.letsgotim.currencyexchanger.model.currencyResponse.ApiData
import retrofit2.Call
import retrofit2.http.*

interface GetCurrency {
    @GET("currency-exchange-rates")
    fun getData(): Call<ApiData>

}