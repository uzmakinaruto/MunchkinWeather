package com.hje.jan.munchkinweather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hje.jan.munchkinweather.logic.Repository
import com.hje.jan.munchkinweather.ui.fragment.WeatherFragment

class WeatherActivityViewModel : ViewModel() {

    private val _isRefresh = MutableLiveData<Unit>()
    val isRefresh = Transformations.switchMap(_isRefresh) {
        Repository.refreshWeathers()
    }

    fun refreshWeathers() {
        _isRefresh.value = _isRefresh.value
    }

    val fragments = mutableListOf<WeatherFragment>()
    val locations = Repository.locations
    var currentPosition = 0
    var clapBoardAlpha = 0.0f
}