package com.hje.jan.munchkinweather.logic.network

import android.util.Log
import com.hje.jan.munchkinweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object MunchkinWeatherNetwork {

    private val placeService = ServiceCreator.create(PlaceService::class.java)

    suspend fun searchPlace(query: String) = placeService.searchPlace(query).await()

    /*fun searchPlace(query: String) {
        placeService.searchPlace(query).enqueue(object : Callback<PlaceResponse> {
            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                Log.d("onFailure", t.message)
            }

            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                Log.d("onResponse", response.body().toString())
            }

        })
    }*/

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