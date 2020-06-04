package com.hje.jan.munchkinweather.logic.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hje.jan.munchkinweather.logic.model.WeatherResponse
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class LocationItemBean(
    val name: String,
    val lng: String,
    val lat: String,
    var skyCon: String? = null,
    var temp: Int? = null,
    var isSelected: Boolean = false,
    var position: Int = 0
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    override fun equals(other: Any?): Boolean {
        val otherItem = (other as LocationItemBean)
        return otherItem.name == name
    }
}