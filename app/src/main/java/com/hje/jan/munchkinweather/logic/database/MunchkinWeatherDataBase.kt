package com.hje.jan.munchkinweather.logic.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hje.jan.munchkinweather.MunchkinWeatherApplication
import com.hje.jan.munchkinweather.logic.database.converter.DailyConverter
import com.hje.jan.munchkinweather.logic.database.converter.HourlyConverter
import com.hje.jan.munchkinweather.logic.database.converter.RealtimeConverter

@Database(version = 1, entities = [LocationItemBean::class])
@TypeConverters(RealtimeConverter::class, DailyConverter::class, HourlyConverter::class)
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