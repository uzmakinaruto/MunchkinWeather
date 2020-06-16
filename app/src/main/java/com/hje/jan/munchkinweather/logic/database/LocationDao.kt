package com.hje.jan.munchkinweather.logic.database

import androidx.room.*

/**
 * 数据库的操作类
 * */
@Dao
interface LocationDao {

    /**
     * 添加新地点
     * */
    @Insert
    fun addLocation(location: LocationItemBean)

    /**
     * 查询数据库中所有的地点 先按是否定位排序 再按位置排序
     * */
    @Query("select * from LocationItemBean order by isLocate desc,position")
    fun getLocations(): MutableList<LocationItemBean>

    /**
     * 查询name为"name"的地点
     * */
    @Query("select * from LocationItemBean where name = :name")
    fun getLocationByName(name: String): LocationItemBean?

    /**
     * 删除指定地点
     * */
    @Delete
    fun deleteLocation(location: LocationItemBean)

    /**
     * 根据name删除指定地点
     * */
    @Query("delete from LocationItemBean where name = :name")
    fun deleteLocationByName(name: String)

    /**
     * 更新地点信息
     * */
    @Update
    fun updateLocation(location: LocationItemBean)

    /**
     *查询本地定位的地点
     * */
    @Query("select * from LocationItemBean where isLocate = 1")
    fun getLocateLocation(): LocationItemBean?
}