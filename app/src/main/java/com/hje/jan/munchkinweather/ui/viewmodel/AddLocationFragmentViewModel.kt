package com.hje.jan.munchkinweather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hje.jan.munchkinweather.logic.Repository
import com.hje.jan.munchkinweather.logic.model.PlaceResponse

class AddLocationFragmentViewModel : ViewModel() {
    /**搜索到的地址*/
    private val _places = MutableLiveData<String>()
    val places = Transformations.switchMap(_places) { query ->
        Repository.searchPlace(query)
    }
    val foundedPlaces = mutableListOf<PlaceResponse.Place>()

    fun searchPlace(query: String) {
        _places.value = query
    }

}