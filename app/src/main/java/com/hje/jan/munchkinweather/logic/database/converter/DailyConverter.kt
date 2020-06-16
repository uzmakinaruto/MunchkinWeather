package com.hje.jan.munchkinweather.logic.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.hje.jan.munchkinweather.logic.model.DailyResponse

/**
 *Room不支持非基本类型 为每个非基本类型数据添加Converter
 */
class DailyConverter {
    var gson = Gson()
    @TypeConverter
    fun objectToString(daily: DailyResponse.Daily?): String {
        return gson.toJson(daily)
    }

    @TypeConverter
    fun stringToObject(str: String?): DailyResponse.Daily? {
        return if (null == str) null
        else
            gson.fromJson(str, DailyResponse.Daily::class.java)
    }
}