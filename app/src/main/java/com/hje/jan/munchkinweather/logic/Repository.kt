package com.hje.jan.munchkinweather.logic

import androidx.lifecycle.liveData
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.logic.database.MunchkinWeatherDataBase
import com.hje.jan.munchkinweather.logic.model.PlaceResponse
import com.hje.jan.munchkinweather.logic.model.WeatherResponse
import com.hje.jan.munchkinweather.logic.network.MunchkinWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

object Repository {

    private val locationDao = MunchkinWeatherDataBase.instance.locationDao()

    fun searchPlace(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val response = MunchkinWeatherNetwork.searchPlace(query)
            if (response.status == "ok") {
                Result.success(response.places)
            } else {
                Result.failure(RuntimeException("response status is ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<PlaceResponse.Place>>(e)
        }
        emit(result)
    }
    
    fun refreshWeather(lng: String, lat: String) = liveData(Dispatchers.IO) {
        val result = try {
            coroutineScope {
                /**这里会并发执行*/
                val realtime = async { MunchkinWeatherNetwork.getRealtimeResponse(lng, lat) }
                val daily = async { MunchkinWeatherNetwork.getDailyResponse(lng, lat, 5) }
                val hourly = async { MunchkinWeatherNetwork.getHourlyResponse(lng, lat) }
                /**这里会等待请求结果*/
                val realtimeResponse = realtime.await()
                val dailyResponse = daily.await()
                val hourlyResponse = hourly.await()
                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok" && hourlyResponse.status == "ok") {
                    Result.success(
                        WeatherResponse(
                            realtimeResponse.result.realtime,
                            dailyResponse.result.daily,
                            hourlyResponse.result.hourly
                        )
                    )
                } else {
                    Result.failure(RuntimeException("status is not ok"))
                }
            }
        } catch (e: Exception) {
            Result.failure<WeatherResponse>(e)
        }
        emit(result)
    }


    suspend fun addLocation(location: LocationItemBean) {
        withContext(Dispatchers.IO) {
            locationDao.addLocation(location)
        }
    }

    fun getLocations() = liveData(Dispatchers.IO) {
        emit(locationDao.getLocations())
    }

    suspend fun deleteLocation(name: String) {
        withContext(Dispatchers.IO) {
            locationDao.deleteLocationByName(name)
        }
    }

    suspend fun updateLocation(location: LocationItemBean) {
        withContext(Dispatchers.IO) {
            locationDao.updateLocation(location)
        }
    }
}