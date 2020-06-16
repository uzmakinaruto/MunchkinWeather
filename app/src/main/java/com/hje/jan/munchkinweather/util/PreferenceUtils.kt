package com.hje.jan.munchkinweather.util

import android.content.Context
import com.hje.jan.munchkinweather.MunchkinWeatherApplication

/**
 * SharedPreference工具类
 * */
object PreferenceUtils {
    val preference by lazy {
        MunchkinWeatherApplication.context.getSharedPreferences(
            "MunchkinWeather",
            Context.MODE_PRIVATE
        )
    }

    fun put(key: String, value: Any) {
        val editor = preference.edit()
        when (value) {
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is String -> editor.putString(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            else -> throw RuntimeException("Unsupported params type")
        }
        editor.apply()
    }

    fun putStringSet(key: String, value: Set<String>) {
        preference.edit().putStringSet(key, value).apply()
    }

}