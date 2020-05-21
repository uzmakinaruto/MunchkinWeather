package com.hje.jan.munchkinweather.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.hje.jan.munchkinweather.logic.model.PlaceResponse
import com.hje.jan.munchkinweather.ui.fragment.WeatherFragment

class WeatherActivityViewModel : ViewModel() {

    lateinit var place: PlaceResponse.Place

    val fragments = mutableListOf<WeatherFragment>()
}