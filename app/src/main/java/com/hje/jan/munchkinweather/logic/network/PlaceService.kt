package com.hje.jan.munchkinweather.logic.network

import com.hje.jan.munchkinweather.MunchkinWeatherApplication
import com.hje.jan.munchkinweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    @GET("/v2/place?token?=${MunchkinWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlace(@Query("query") query: String): Call<PlaceResponse>
}