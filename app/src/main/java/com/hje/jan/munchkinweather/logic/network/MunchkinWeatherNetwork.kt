package com.hje.jan.munchkinweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object MunchkinWeatherNetwork {

    private val placeService = ServiceCreator.create<PlaceService>()
    private val weatherService = ServiceCreator.create<WeatherService>()

    suspend fun searchPlace(query: String) = placeService.searchPlace(query).await()

    suspend fun getRealtimeResponse(lng: String, lat: String) =
        weatherService.getRealtimeResponse(lng, lat).await()

    suspend fun getDailyResponse(lng: String, lat: String) =
        weatherService.getDailyResponse(lng, lat).await()

    suspend fun getHourlyResponse(lng: String, lat: String) =
        weatherService.getHourlyResponse(lng, lat).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (null != body) {
                        continuation.resume(body)
                    } else {
                        continuation.resumeWithException(RuntimeException("Body is null"))
                    }
                }
            })
        }
    }
}