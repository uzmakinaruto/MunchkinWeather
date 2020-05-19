package com.hje.jan.munchkinweather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hje.jan.munchkinweather.logic.Repository
import com.hje.jan.munchkinweather.logic.model.Place

class PlaceViewModel : ViewModel() {

    private val searchPlace = MutableLiveData<String>()

    val places = Transformations.switchMap(searchPlace) { query ->
        Repository.searchPlace(query)
    }


    val foundedPlaces = mutableListOf<Place>()

    fun searchPlace(query: String) {
        searchPlace.value = query
    }
}