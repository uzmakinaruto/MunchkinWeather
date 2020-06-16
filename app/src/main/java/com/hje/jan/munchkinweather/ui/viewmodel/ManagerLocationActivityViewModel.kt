package com.hje.jan.munchkinweather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.amap.api.location.AMapLocation
import com.hje.jan.munchkinweather.logic.Repository
import com.hje.jan.munchkinweather.logic.database.LocationItemBean

class ManagerLocationActivityViewModel : ViewModel() {

    private val _isLocate = MutableLiveData<LocationItemBean>()
    //private val _locations = MutableLiveData<Any?>()

    val isLocate = Transformations.switchMap(_isLocate) { locateLoaction ->
        Repository.getLocateWeatherInfo(locateLoaction)
    }


    //val locations = Repository.locations.value!!
    /**添加地址*/
    fun addLocation(location: LocationItemBean) {
        //添加到livedata
        Repository.locations.value?.add(location)
        //刷新livedata
        Repository.refreshLocations()
        //添加到数据库
        Repository.addLocation(location)
    }

    //val locations = Repository.locations.value!!
    /**添加地址*/
    fun addLocationWithoutRefresh(location: LocationItemBean) {
        //添加到livedata
        Repository.locations.value?.add(location)
        //添加到数据库
        Repository.addLocation(location)
    }

    fun getLocationWeatherInfo(location: LocationItemBean) {
        Repository.getLocationWeatherInfo(location)
    }

    /**删除地址*/
    fun deleteLocation(location: LocationItemBean) {
        //从livedata删除
        Repository.locations.value?.remove(location)
        //刷新livedata
        Repository.refreshLocations()
        //从数据库删除
        Repository.deleteLocation(location.name)
    }


    fun updateLocation(location: LocationItemBean) {
        //刷新liveData
        Repository.refreshLocations()
        //更新数据库
        Repository.updateLocation(location)
    }

    fun getLocateWeatherInfo(locateLocation: LocationItemBean) {
        _isLocate.value = locateLocation
    }

    fun updateLocateLocation(aMapLocation: AMapLocation) {
        Repository.locations.value?.let { locations ->
            locations[0].name = aMapLocation.city
            locations[0].lng = aMapLocation.longitude.toString()
            locations[0].lat = aMapLocation.latitude.toString()
        }
    }

    fun getLocations() = Repository.locations.value!!
    fun refreshLocations() = Repository.refreshLocations()
}