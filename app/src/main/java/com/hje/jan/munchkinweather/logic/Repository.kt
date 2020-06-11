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
                val daily = async { MunchkinWeatherNetwork.getDailyResponse(lng, lat, 15) }
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

    /*suspend fun getLocationWeatherInfo(location: LocationItemBean) {
        withContext(Dispatchers.IO) {
            val response = MunchkinWeatherNetwork.getRealtimeResponse(location.lng, location.lat)
            if (response.status == "ok") {
                location.temp = response.result.realtime.temperature.toInt()
                location.skyCon = response.result.realtime.skycon
                val lib = getLocationByName(location.name)
                if (null != lib) {
                    lib.temp = response.result.realtime.temperature.toInt()
                    lib.skyCon = response.result.realtime.skycon
                    updateLocation(lib)
                }
            }
        }
    }*/

    suspend fun getLocationWeatherInfo(location: LocationItemBean): Boolean {
        return withContext(Dispatchers.IO) {
            var result = false
            val realtime =
                async { MunchkinWeatherNetwork.getRealtimeResponse(location.lng, location.lat) }
            val daily =
                async { MunchkinWeatherNetwork.getDailyResponse(location.lng, location.lat, 15) }
            val hourly =
                async { MunchkinWeatherNetwork.getHourlyResponse(location.lng, location.lat) }
            /**这里会等待请求结果*/
            val realtimeResponse = realtime.await()
            val dailyResponse = daily.await()
            val hourlyResponse = hourly.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok" && hourlyResponse.status == "ok") {
                /**传入的location不一定从database找出来的 需要从数据库找到对应location,再进行更新*/
                val sqlLocation = getLocationByName(location.name)
                if (sqlLocation != null) {
                    sqlLocation.realTime = realtimeResponse.result.realtime
                    sqlLocation.hourly = hourlyResponse.result.hourly
                    sqlLocation.daily = dailyResponse.result.daily
                    updateLocation(sqlLocation)
                    result = true
                }
            }
            result
        }
    }

    fun getWeatherInfo() {

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

    private fun getLocationByName(name: String): LocationItemBean? {
        return locationDao.getLocationByName(name)
    }

    fun setLocateLocation(name: String, lng: String, lat: String) =
        liveData(Dispatchers.IO) {
            var result = false
            var locateLocation = locationDao.getLocateLocation()!!
            locateLocation.name = name
            locateLocation.lng = lng
            locateLocation.lat = lat
            locateLocation.isLocateEnable = true
            updateLocation(locateLocation)
            result = getLocationWeatherInfo(locateLocation)
            /*val response =
                MunchkinWeatherNetwork.getRealtimeResponse(
                    locateLocation.lng,
                    locateLocation.lat
                )
            if (response.status == "ok") {
                locateLocation.temp = response.result.realtime.temperature.toInt()
                locateLocation.skyCon = response.result.realtime.skycon
                updateLocation(locateLocation)
                result = true
            }*/
            emit(result)
        }
}