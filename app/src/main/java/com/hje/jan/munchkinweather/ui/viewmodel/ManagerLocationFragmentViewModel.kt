package com.hje.jan.munchkinweather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hje.jan.munchkinweather.logic.Repository
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ManagerLocationFragmentViewModel : ViewModel() {

    private val _selectedLocations = MutableLiveData<Unit>()
    private val job = Job()
    val selectLocations = Transformations.switchMap(_selectedLocations) {
        Repository.getLocations()
    }

    fun addLocation(location: LocationItemBean) {
        CoroutineScope(job).launch {
            Repository.addLocation(location)
            refreshLocations()
        }
    }

    fun deleteLocation(name: String) {
        CoroutineScope(job).launch {
            Repository.deleteLocation(name)
            refreshLocations()
        }
    }

    fun refreshLocations() {
        _selectedLocations.postValue(null)
    }

    fun updateLocation(location: LocationItemBean) {
        CoroutineScope(job).launch {
            Repository.updateLocation(location)
        }
    }

    var locations: MutableList<LocationItemBean> = mutableListOf()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}