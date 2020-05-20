package com.hje.jan.munchkinweather.logic.network

import com.hje.jan.munchkinweather.MunchkinWeatherApplication
import com.hje.jan.munchkinweather.logic.model.DailyResponse
import com.hje.jan.munchkinweather.logic.model.HourlyResponse
import com.hje.jan.munchkinweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("/v2.5/${MunchkinWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeResponse(@Path("lng") lat: String, @Path("lat") lng: String): Call<RealtimeResponse>

    @GET("/v2.5/${MunchkinWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyResponse(@Path("lng") lat: String, @Path("lat") lng: String): Call<DailyResponse>

    @GET("/v2.5/${MunchkinWeatherApplication.TOKEN}/{lng},{lat}/hourly.json")
    fun getHourlyResponse(@Path("lng") lat: String, @Path("lat") lng: String): Call<HourlyResponse>
}