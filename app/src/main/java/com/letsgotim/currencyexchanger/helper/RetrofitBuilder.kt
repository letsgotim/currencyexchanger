package com.letsgotim.currencyexchanger.helper


import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val header = Interceptor { chain ->
        var request = chain.request().newBuilder().build()
        chain.proceed(request)
    }

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(header)
        .addInterceptor(logger)
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)

    //https://developers.paysera.com/tasks/api/currency-exchange-rates

    private val builder = Retrofit.Builder()
        .baseUrl("https://developers.paysera.com/tasks/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())
    private val retrofit = builder.build()

    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }

}