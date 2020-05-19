package com.hje.jan.munchkinweather.logic.model

import com.google.gson.annotations.SerializedName

data class Place(
    val id: String,
    val location: Location,
    @SerializedName("place_id") val placeId: String,
    val name: String,
    @SerializedName("formatted_address") val formattedAddress: String
)