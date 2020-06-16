package com.hje.jan.munchkinweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 网络方法封装类
 * */
object MunchkinWeatherNetwork {

    /**
     * 地点service
     * */
    private val placeService = ServiceCreator.create<PlaceService>()
    /**
     * 天气service
     * */
    private val weatherService = ServiceCreator.create<WeatherService>()

    /**
     * 查询地点
     * */
    suspend fun searchPlace(query: String) = placeService.searchPlace(query).await()

    /**
     * 查询实时天气
     * */
    suspend fun getRealtimeResponse(lng: String, lat: String) =
        weatherService.getRealtimeResponse(lng, lat).await()

    /**
     * 查询未来几天天气
     * */
    suspend fun getDailyResponse(lng: String, lat: String, requestDay: Int = 15) =
        weatherService.getDailyResponse(lng, lat, requestDay).await()

    /**
     * 查询未来几小时天气
     * */
    suspend fun getHourlyResponse(lng: String, lat: String) =
        weatherService.getHourlyResponse(lng, lat).await()


    /**
     * 封装成功与异常处理
     * */
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