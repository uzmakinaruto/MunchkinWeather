package com.hje.jan.munchkinweather.logic.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hje.jan.munchkinweather.MunchkinWeatherApplication

@Database(version = 1, entities = [LocationItemBean::class])
abstract class MunchkinWeatherDataBase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object {
        val instance by lazy {
            Room.databaseBuilder(
                MunchkinWeatherApplication.context,
                MunchkinWeatherDataBase::class.java,
                "munchkin_weather"
            ).build()
        }
    }
}