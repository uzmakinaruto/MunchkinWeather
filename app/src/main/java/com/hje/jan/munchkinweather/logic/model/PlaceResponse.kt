package com.hje.jan.munchkinweather.logic.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * 搜索到的地址信息(彩云天气API提供)
 * */
data class PlaceResponse(
    val status: String,
    val query: String,
    val places: List<Place>
) {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Place(
        val id: String,
        val location: Location,
        @SerializedName("place_id") val placeId: String,
        val name: String,
        @SerializedName("formatted_address") val formattedAddress: String
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Location(
        val lng: String,
        val lat: String
    ) : Parcelable
}


