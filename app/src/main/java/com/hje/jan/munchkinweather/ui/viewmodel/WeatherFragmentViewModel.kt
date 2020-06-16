package com.hje.jan.munchkinweather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hje.jan.munchkinweather.logic.Repository
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.logic.model.PlaceResponse

class WeatherFragmentViewModel : ViewModel() {

    private val locationLiveData = MutableLiveData<PlaceResponse.Location>()/*
    val weatherLiveData = Transformations.switchMap(locationLiveData) {
        Repository.refreshWeather(it.lng, it.lat)
    }*/

    private val _isRefresh = MutableLiveData<Unit>()

    val isRefresh = Transformations.switchMap(_isRefresh) {
        Repository.refreshWeathers()
    }

    fun refreshWeathers() {
        _isRefresh.value = _isRefresh.value
    }

    /*fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = PlaceResponse.Location(lng, lat)
    }*/
    
    lateinit var location: LocationItemBean

    fun updateLocation() {
        Repository.updateLocation(location)
    }
}