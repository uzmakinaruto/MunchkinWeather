package com.hje.jan.munchkinweather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hje.jan.munchkinweather.logic.Repository
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.logic.model.PlaceResponse
import com.hje.jan.munchkinweather.ui.fragment.WeatherFragment

class WeatherActivityViewModel : ViewModel() {

    val fragments = mutableListOf<WeatherFragment>()

    private val _selectedLocations = MutableLiveData<Unit>()

    val selectLocations = Transformations.switchMap(_selectedLocations) {
        Repository.getLocations()
    }

    fun refreshLocations() {
        _selectedLocations.postValue(null)
    }

}