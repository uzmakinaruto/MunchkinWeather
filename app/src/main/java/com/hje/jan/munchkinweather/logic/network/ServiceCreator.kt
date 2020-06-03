package com.hje.jan.munchkinweather.logic.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {

    private val okHttpClient = OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build()

    private val retrofit = Retrofit.Builder().baseUrl("https://api.caiyunapp.com")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()


    fun <T> create(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

    inline fun <reified T> create(): T = create(T::class.java)
}