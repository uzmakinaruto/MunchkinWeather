package com.hje.jan.munchkinweather.logic.database

import androidx.room.*

@Dao
interface LocationDao {

    @Insert
    fun addLocation(location: LocationItemBean)

    /**先按是否定位排序 再按位置排序*/
    @Query("select * from LocationItemBean order by isLocate desc,position")
    fun getLocations(): MutableList<LocationItemBean>

    @Query("select * from LocationItemBean where name = :name")
    fun getLocationByName(name: String): LocationItemBean?

    @Delete
    fun deleteLocation(location: LocationItemBean)

    @Query("delete from LocationItemBean where name = :name")
    fun deleteLocationByName(name: String)

    @Update
    fun updateLocation(location: LocationItemBean)

    @Query("select * from LocationItemBean where isLocate = 1")
    fun getLocateLocation(): LocationItemBean?
}