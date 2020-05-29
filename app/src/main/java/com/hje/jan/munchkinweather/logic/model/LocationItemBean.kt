package com.hje.jan.munchkinweather.logic.model

data class LocationItemBean(
    val name: String,
    val lng: String,
    val lat: String,
    var skyCon: String? = null,
    var temp: Int? = null,
    var isSelected: Boolean = false
) {

    override fun equals(other: Any?): Boolean {
        return (other as LocationItemBean).name == name
    }
}