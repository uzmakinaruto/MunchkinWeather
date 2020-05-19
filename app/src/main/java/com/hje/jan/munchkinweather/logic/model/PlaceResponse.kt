package com.hje.jan.munchkinweather.logic.model

data class PlaceResponse(
    val status: String,
    val query: String,
    val places: List<Place>
)