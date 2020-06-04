package com.hje.jan.munchkinweather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hje.jan.munchkinweather.logic.Repository
import com.hje.jan.munchkinweather.logic.model.PlaceResponse
import kotlinx.coroutines.Job

class AddLocationFragmentViewModel : ViewModel() {

    private val job = Job()
    /**搜索到的地址*/
    private val _places = MutableLiveData<String>()
    val places = Transformations.switchMap(_places) { query ->
        Repository.searchPlace(query)
    }
    val foundedPlaces = mutableListOf<PlaceResponse.Place>()

    fun searchPlace(query: String) {
        _places.value = query
    }

    /**选中的地址
    private val _selectedLocations = MutableLiveData<Unit>()

    val selectLocations = Transformations.switchMap(_selectedLocations) {
    Repository.getLocations()
    }

    fun refreshLocations() {
    _selectedLocations.value = _selectedLocations.value
    }

    val locations: MutableList<LocationItemBean> = mutableListOf()

     */
    /**添加地址到数据库*//*
    fun addLocation(location: LocationItemBean) {
        CoroutineScope(job).launch {
            Repository.addLocation(location)
        }
    }

    */
    /**从数据库中删除地址*//*
    fun deleteLocation(name: String) {
        CoroutineScope(job).launch {
            Repository.deleteLocation(name)
        }
    }*/
    
    /**释放协程*/
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}