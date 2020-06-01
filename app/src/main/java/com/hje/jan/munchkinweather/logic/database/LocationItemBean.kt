package com.hje.jan.munchkinweather.logic.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class LocationItemBean(
    val name: String,
    val lng: String,
    val lat: String,
    var skyCon: String? = null,
    var temp: Int? = null,
    var isSelected: Boolean = false
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    override fun equals(other: Any?): Boolean {
        return (other as LocationItemBean).name == name
    }
}