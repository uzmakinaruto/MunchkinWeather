package com.hje.jan.munchkinweather.logic.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationItemBean(
    val name: String,
    val lng: String,
    val lat: String,
    var skyCon: String? = null,
    var temp: Int? = null,
    var isSelected: Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    
    override fun equals(other: Any?): Boolean {
        return (other as LocationItemBean).name == name
    }
}