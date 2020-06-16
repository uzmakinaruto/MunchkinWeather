package com.hje.jan.munchkinweather.logic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.logic.database.MunchkinWeatherDataBase
import com.hje.jan.munchkinweather.logic.model.*
import com.hje.jan.munchkinweather.logic.network.MunchkinWeatherNetwork
import kotlinx.coroutines.*

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

    /**
     * 读取数据库中的locations到liveData对象中
     * */
    fun getDatabaseLocations() {
        CoroutineScope(job).launch(Dispatchers.IO) {
            locations.postValue(locationDao.getLocations())
        }
    }

    /**
     * 刷新天气信息
     * */
    fun refreshWeather(lng: String, lat: String) = liveData(Dispatchers.IO) {
        val result = try {
            coroutineScope {
                /**这里会并发执行*/
                val realtime = async { MunchkinWeatherNetwork.getRealtimeResponse(lng, lat) }
                val daily = async { MunchkinWeatherNetwork.getDailyResponse(lng, lat) }
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

    /**
     *获取指定location的天气信息,返回result
     * */
    suspend fun getLocationWeatherInfoWithResult(location: LocationItemBean): Boolean {
        return withContext(Dispatchers.IO) {
            var result = false
            val realtime =
                async { MunchkinWeatherNetwork.getRealtimeResponse(location.lng, location.lat) }
            val daily =
                async {
                    MunchkinWeatherNetwork.getDailyResponse(
                        location.lng,
                        location.lat
                    )
                }
            val hourly =
                async { MunchkinWeatherNetwork.getHourlyResponse(location.lng, location.lat) }
            /**这里会等待请求结果*/
            val realtimeResponse = realtime.await()
            val dailyResponse = daily.await()
            val hourlyResponse = hourly.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok" && hourlyResponse.status == "ok") {
                //更新内存里的location天气信息
                location.isLocateEnable = true
                location.realTime = realtimeResponse.result.realtime
                location.hourly = hourlyResponse.result.hourly
                location.daily = dailyResponse.result.daily
                //保存到数据库
                updateLocation(location)
                result = true
            }
            result
        }
    }

    /**
     * 获取指定location的天气信息,成功则保存天气信息,不关心返回状态
     * */
    fun getLocationWeatherInfo(location: LocationItemBean) {
        CoroutineScope(job).launch {
            withContext(Dispatchers.IO) {
                val realtime =
                    async { MunchkinWeatherNetwork.getRealtimeResponse(location.lng, location.lat) }
                val daily =
                    async {
                        MunchkinWeatherNetwork.getDailyResponse(
                            location.lng,
                            location.lat
                        )
                    }
                val hourly =
                    async { MunchkinWeatherNetwork.getHourlyResponse(location.lng, location.lat) }
                /**这里会等待请求结果*/
                val realtimeResponse = realtime.await()
                val dailyResponse = daily.await()
                val hourlyResponse = hourly.await()
                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok" && hourlyResponse.status == "ok") {
                    //更新liveData里的location天气信息
                    location.realTime = realtimeResponse.result.realtime
                    location.hourly = hourlyResponse.result.hourly
                    location.daily = dailyResponse.result.daily
                    //刷新liveData
                    refreshLocations()
                    val sqlLocation = getLocationByName(location.name)
                    if (sqlLocation != null) {
                        sqlLocation.realTime = realtimeResponse.result.realtime
                        sqlLocation.hourly = hourlyResponse.result.hourly
                        sqlLocation.daily = dailyResponse.result.daily
                        updateLocation(sqlLocation)
                    }
                }
            }
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
     * 获取本地定位的天气信息,返回liveData
     * */
    fun getLocateWeatherInfo(locateLocation: LocationItemBean) =
        liveData(Dispatchers.IO) {
            var result = getLocationWeatherInfoWithResult(locateLocation)
            emit(result)
        }

    fun refreshWeathers() = liveData(Dispatchers.IO) {
        val results = withContext(Dispatchers.IO) {
            val realtimeDeferreds = mutableListOf<Deferred<RealtimeResponse>>()
            val dailyDeferreds = mutableListOf<Deferred<DailyResponse>>()
            val hourlyDeferreds = mutableListOf<Deferred<HourlyResponse>>()
            val results = mutableListOf<Boolean>()
            for (location in locations.value!!) {
                realtimeDeferreds.add(async {
                    MunchkinWeatherNetwork.getRealtimeResponse(
                        location.lng,
                        location.lat
                    )
                })
                dailyDeferreds.add(async {
                    MunchkinWeatherNetwork.getDailyResponse(
                        location.lng,
                        location.lat
                    )
                })
                hourlyDeferreds.add(async {
                    MunchkinWeatherNetwork.getHourlyResponse(
                        location.lng,
                        location.lat
                    )
                })
            }
            for (index in 0 until locations.value!!.size) {
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
            results
        }
        emit(results)
    }
}