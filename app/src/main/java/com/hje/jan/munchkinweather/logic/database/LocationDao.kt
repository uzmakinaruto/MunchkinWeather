package com.hje.jan.munchkinweather.logic.database

import androidx.room.*

@Dao
interface LocationDao {

    @Insert
    fun addLocation(location: LocationItemBean)

    @Query("select * from LocationItemBean order by position")
    fun getLocations(): MutableList<LocationItemBean>

    @Delete
    fun deleteLocation(location: LocationItemBean)

    @Query("delete from LocationItemBean where name = :name")
    fun deleteLocationByName(name: String)

    @Update
    fun updateLocation(location: LocationItemBean)
}