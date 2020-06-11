package com.hje.jan.munchkinweather.logic.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.hje.jan.munchkinweather.logic.model.DailyResponse
import com.hje.jan.munchkinweather.logic.model.HourlyResponse


class HourlyConverter {
    var gson = Gson()
    @TypeConverter
    fun objectToString(hourly: HourlyResponse.Hourly?): String {
        return gson.toJson(hourly)
    }

    @TypeConverter
    fun stringToObject(str: String?): HourlyResponse.Hourly? {
        return if (null == str) null
        else
            gson.fromJson(str, HourlyResponse.Hourly::class.java)
    }
}