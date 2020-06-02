package com.hje.jan.munchkinweather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hje.jan.munchkinweather.logic.Repository
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.logic.model.DailyResponse
import com.hje.jan.munchkinweather.logic.model.HourlyResponse
import com.hje.jan.munchkinweather.logic.model.PlaceResponse
import com.hje.jan.munchkinweather.logic.model.RealtimeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WeatherFragmentViewModel : ViewModel() {

    private val locationLiveData = MutableLiveData<PlaceResponse.Location>()
    private val job = Job()
    val weatherLiveData = Transformations.switchMap(locationLiveData) {
        Repository.refreshWeather(it.lng, it.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = PlaceResponse.Location(lng, lat)
    }

    var realtimeResult: RealtimeResponse.Realtime? = null
    var dailyResult: DailyResponse.Daily? = null
    var hourResult: HourlyResponse.Hourly? = null

    lateinit var location: LocationItemBean

    fun updateLocation() {
        CoroutineScope(job).launch {
            Repository.updateLocation(location)
        }
    }

}