package com.hje.jan.munchkinweather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.amap.api.location.AMapLocation
import com.hje.jan.munchkinweather.logic.Repository
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ManagerLocationActivityViewModel : ViewModel() {

    private val _isLocate = MutableLiveData<AMapLocation>()
    private val _locations = MutableLiveData<Any?>()
    private val job = Job()

    val locationsLiveData = Transformations.switchMap(_locations) {
        Repository.getLocations()
    }

    val isLocate = Transformations.switchMap(_isLocate) {
        Repository.setLocateLocation(it.city, it.longitude.toString(), it.latitude.toString())
    }
    fun getLocations() {
        _locations.value = _locations.value
    }

    val locations = mutableListOf<LocationItemBean>()
    /**添加地址到数据库*/
    fun addLocation(location: LocationItemBean) {
        CoroutineScope(job).launch {
            Repository.addLocation(location)
        }
    }

    fun getLocationWeatherInfo(location: LocationItemBean) {
        CoroutineScope(job).launch {
            Repository.getLocationWeatherInfo(location)
        }
    }


    /**从数据库中删除地址*/
    fun deleteLocation(name: String) {
        CoroutineScope(job).launch {
            Repository.deleteLocation(name)
        }
    }


    fun updateLocation(location: LocationItemBean) {
        CoroutineScope(job).launch {
            Repository.updateLocation(location)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun setLocateLocation(aMapLocation: AMapLocation) {
        _isLocate.value = aMapLocation
    }
}