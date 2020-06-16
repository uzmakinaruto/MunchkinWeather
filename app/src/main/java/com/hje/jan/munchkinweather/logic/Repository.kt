package com.hje.jan.munchkinweather.logic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.logic.database.MunchkinWeatherDataBase
import com.hje.jan.munchkinweather.logic.model.DailyResponse
import com.hje.jan.munchkinweather.logic.model.HourlyResponse
import com.hje.jan.munchkinweather.logic.model.RealtimeResponse
import com.hje.jan.munchkinweather.logic.network.MunchkinWeatherNetwork
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Repository 负责viewModel和model层间沟通桥梁
 * */
object Repository {

    /**
     * 数据库操作对象
     * */
    private val locationDao = MunchkinWeatherDataBase.instance.locationDao()
    private val job = Job()

    /**
     * LocationItemBean的liveData对象,在WeatherActivity中观察,
     * 当数据有变化时,在WeatherActivity刷新界面
     * */
    val locations = MutableLiveData<MutableList<LocationItemBean>>()

    /**
     * 刷新locations
     * */
    fun refreshLocations() {
        locations.postValue(locations.value)
    }

    /**
     * 搜索地址返回liveData对象以供观察
     * */
    fun searchPlace(query: String) = fire(Dispatchers.IO) {
        val response = MunchkinWeatherNetwork.searchPlace(query)
        if (response.status == "ok") {
                Result.success(response.places)
            } else {
                Result.failure(RuntimeException("response status is ${response.status}"))
            }
    }

    /**
     * 读取数据库中的locations到liveData对象中
     * */
    fun getDatabaseLocations() {
        CoroutineScope(job).launch(Dispatchers.IO) {
            locations.postValue(locationDao.getLocations())
        }
    }

    /**
     * 添加location到数据库
     * */
    fun addLocation(location: LocationItemBean) {
        CoroutineScope(job).launch {
            withContext(Dispatchers.IO) {
                locationDao.addLocation(location)
            }
        }
    }

    /**
     * 删除名字为name的location
     * */
    fun deleteLocation(name: String) {
        CoroutineScope(job).launch {
            withContext(Dispatchers.IO) {
                locationDao.deleteLocationByName(name)
            }
        }
    }

    /**
     * 更新指定的location信息
     * */
    fun updateLocation(location: LocationItemBean) {
        CoroutineScope(job).launch {
            withContext(Dispatchers.IO) {
                locationDao.updateLocation(location)
            }
        }
    }

    /**
     * 通过name查找location
     * */
    private fun getLocationByName(name: String): LocationItemBean? {
        return locationDao.getLocationByName(name)
    }


    /**
     * 获取天气信息,返回liveData
     * */
    fun getLocationWeatherInfo(location: LocationItemBean) =
        fire(Dispatchers.IO) {
            coroutineScope {
                val realtime =
                    async {
                        MunchkinWeatherNetwork.getRealtimeResponse(
                            location.lng,
                            location.lat
                        )
                    }
                val daily =
                    async {
                        MunchkinWeatherNetwork.getDailyResponse(
                            location.lng,
                            location.lat
                        )
                    }
                val hourly =
                    async {
                        MunchkinWeatherNetwork.getHourlyResponse(
                            location.lng,
                            location.lat
                        )
                    }
                /**这里会等待请求结果*/
                val realtimeResponse = realtime.await()
                val dailyResponse = daily.await()
                val hourlyResponse = hourly.await()
                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok" && hourlyResponse.status == "ok") {
                    //更新内存里的location天气信息
                    location.realTime = realtimeResponse.result.realtime
                    location.hourly = hourlyResponse.result.hourly
                    location.daily = dailyResponse.result.daily
                    if (location.isLocate) {
                        location.isLocateEnable = true
                        updateLocation(location)
                    } else {
                        getLocationByName(location.name)?.let {
                            it.realTime = realtimeResponse.result.realtime
                            it.hourly = hourlyResponse.result.hourly
                            it.daily = dailyResponse.result.daily
                            updateLocation(it)
                        }
                    }
                    refreshLocations()
                    Result.success(
                        true
                    )
                } else {
                    Result.failure(RuntimeException("status is not ok"))
                }
            }
        }

    fun refreshWeathers() = fire(Dispatchers.IO) {
        coroutineScope {
            val startIndex = if (locations.value!![0].isLocateEnable) 0
            else 1
            val realtimeDeferreds = mutableListOf<Deferred<RealtimeResponse>>()
            val dailyDeferreds = mutableListOf<Deferred<DailyResponse>>()
            val hourlyDeferreds = mutableListOf<Deferred<HourlyResponse>>()
            val results = mutableListOf<Boolean>()
            for (index in startIndex until locations.value!!.size) {
                realtimeDeferreds.add(async {
                    MunchkinWeatherNetwork.getRealtimeResponse(
                        locations.value!![index].lng,
                        locations.value!![index].lat
                    )
                })
                dailyDeferreds.add(async {
                    MunchkinWeatherNetwork.getDailyResponse(
                        locations.value!![index].lng,
                        locations.value!![index].lat
                    )
                })
                hourlyDeferreds.add(async {
                    MunchkinWeatherNetwork.getHourlyResponse(
                        locations.value!![index].lng,
                        locations.value!![index].lat
                    )
                })
            }
            for (index in 0 until realtimeDeferreds.size) {
                val realtime = realtimeDeferreds[index].await()
                val daily = dailyDeferreds[index].await()
                val hourly = hourlyDeferreds[index].await()
                if (realtime.status == "ok" && daily.status == "ok"
                    && hourly.status == "ok"
                ) {
                    locations.value!![index].realTime = realtime.result.realtime
                    locations.value!![index].hourly = hourly.result.hourly
                    locations.value!![index].daily = daily.result.daily
                    //保存到数据库
                    updateLocation(locations.value!![index])
                    results.add(true)
                } else {
                    results.add(false)
                }
            }
            Result.success(results)
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
}