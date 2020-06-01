package com.hje.jan.munchkinweather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hje.jan.munchkinweather.logic.Repository
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.logic.model.PlaceResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddLocationFragmentViewModel : ViewModel() {


    private val searchPlace = MutableLiveData<String>()
    private val job = Job()
    val places = Transformations.switchMap(searchPlace) { query ->
        Repository.searchPlace(query)
    }


    val foundedPlaces = mutableListOf<PlaceResponse.Place>()

    fun searchPlace(query: String) {
        searchPlace.value = query
    }

    private val _selectedLocations = MutableLiveData<Unit>()

    val selectLocations = Transformations.switchMap(_selectedLocations) {
        Repository.getLocations()
    }

    fun addLocation(location: LocationItemBean) {
        CoroutineScope(job).launch {
            Repository.addLocation(location)
        }

    }

    fun deleteLocation(name: String) {
        CoroutineScope(job).launch {
            Repository.deleteLocation(name)
        }
    }

    fun refreshLocations() {
        _selectedLocations.value = _selectedLocations.value
    }

    var locations: MutableList<LocationItemBean> = mutableListOf()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}