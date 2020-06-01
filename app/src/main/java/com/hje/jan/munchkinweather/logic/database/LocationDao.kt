package com.hje.jan.munchkinweather.logic.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {

    @Insert
    fun addLocation(location: LocationItemBean)

    @Query("select * from LocationItemBean")
    fun getLocations(): MutableList<LocationItemBean>

    @Delete
    fun deleteLocation(location: LocationItemBean)

    @Query("delete from LocationItemBean where name = :name")
    fun deleteLocationByName(name: String)
}