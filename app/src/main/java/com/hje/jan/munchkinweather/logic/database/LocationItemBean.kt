package com.hje.jan.munchkinweather.logic.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hje.jan.munchkinweather.logic.database.converter.RealtimeConverter
import com.hje.jan.munchkinweather.logic.model.DailyResponse
import com.hje.jan.munchkinweather.logic.model.HourlyResponse
import com.hje.jan.munchkinweather.logic.model.RealtimeResponse
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class LocationItemBean(
    var name: String,
    var lng: String,
    var lat: String,
    var isSelected: Boolean = false,
    var position: Int = 0,
    var isLocate: Boolean = false,
    var isLocateEnable: Boolean = false,
    var realTime: RealtimeResponse.Realtime? = null,
    var daily: DailyResponse.Daily? = null,
    var hourly: HourlyResponse.Hourly? = null
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    override fun equals(other: Any?): Boolean {
        val otherItem = (other as LocationItemBean)
        return otherItem.name == name
    }
}