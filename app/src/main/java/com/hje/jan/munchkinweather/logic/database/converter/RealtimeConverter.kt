package com.hje.jan.munchkinweather.logic.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.hje.jan.munchkinweather.logic.model.RealtimeResponse


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