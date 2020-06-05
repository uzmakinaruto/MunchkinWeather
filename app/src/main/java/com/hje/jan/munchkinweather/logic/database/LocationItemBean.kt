package com.hje.jan.munchkinweather.logic.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class LocationItemBean(
    var name: String,
    var lng: String,
    var lat: String,
    var skyCon: String? = null,
    var temp: Int? = null,
    var isSelected: Boolean = false,
    var position: Int = 0,
    var isLocate: Boolean = false,
    var isLocateEnable: Boolean = false
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    override fun equals(other: Any?): Boolean {
        val otherItem = (other as LocationItemBean)
        return otherItem.name == name
    }
}