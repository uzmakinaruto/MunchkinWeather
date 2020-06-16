package com.hje.jan.munchkinweather.logic.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.hje.jan.munchkinweather.logic.model.RealtimeResponse

/**
 *Room不支持非基本类型 为每个非基本类型数据添加Converter
 */
class RealtimeConverter {
    var gson = Gson()
    @TypeConverter
    fun objectToString(realtime: RealtimeResponse.Realtime?): String {
        return gson.toJson(realtime)
    }

    @TypeConverter
    fun stringToObject(str: String?): RealtimeResponse.Realtime? {
        return if (null == str) null
        else
            gson.fromJson(str, RealtimeResponse.Realtime::class.java)
    }
}