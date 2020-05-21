package com.hje.jan.munchkinweather.util

import com.hje.jan.munchkinweather.R

/***
 *
晴（白天）	CLEAR_DAY	cloudrate < 0.2
晴（夜间）	CLEAR_NIGHT	cloudrate < 0.2
多云（白天）	PARTLY_CLOUDY_DAY	0.8 >= cloudrate > 0.2
多云（夜间）	PARTLY_CLOUDY_NIGHT	0.8 >= cloudrate > 0.2
阴	CLOUDY	cloudrate > 0.8
轻度雾霾	LIGHT_HAZE	PM2.5 100~150
中度雾霾	MODERATE_HAZE	PM2.5 150~200
重度雾霾	HEAVY_HAZE	PM2.5 > 200
小雨	LIGHT_RAIN
中雨	MODERATE_RAIN
大雨	HEAVY_RAIN
暴雨	STORM_RAIN
雾	FOG	能见度低，湿度高，风速低，温度低
小雪	LIGHT_SNOW
中雪	MODERATE_SNOW
大雪	HEAVY_SNOW
暴雪	STORM_SNOW
浮尘	DUST	aqi > 150，pm10 > 150，湿度 < 30%，风速 < 6 m/s
沙尘	SAND	aqi > 150，pm10 > 150，湿度 < 30%，风速 > 6 m/s
大风	WIND
 */

/**
AQI数值	空气质量等级
0 ~50	优
50~100	良
100~150	轻度污染
150~200	中度污染
>200	重度污染*/

object WeatherUtil {
    val SKYCONS = listOf(
        "CLEAR_DAY",
        "CLEAR_NIGHT",
        "PARTLY_CLOUDY_DAY",
        "PARTLY_CLOUDY_NIGHT",
        "CLOUDY",
        "LIGHT_HAZE",
        "MODERATE_HAZE",
        "HEAVY_HAZE",
        "LIGHT_RAIN",
        "MODERATE_RAIN",
        "HEAVY_RAIN",
        "STORM_RAIN",
        "FOG",
        "LIGHT_SNOW",
        "MODERATE_SNOW",
        "HEAVY_SNOW",
        "STORM_SNOW",
        "DUST",
        "SAND",
        "WIND"
    )

    fun getVideoNameBySkyCon(skyCon: String): String {
        return when (skyCon) {
            "CLEAR_DAY" -> "sun.mp4"
            "CLEAR_NIGHT" -> "sun.mp4"
            "PARTLY_CLOUDY_DAY" -> "cloud.mp4"
            "PARTLY_CLOUDY_NIGHT" -> "cloud.mp4"
            "CLOUDY" -> "cloud.mp4"
            "LIGHT_HAZE" -> "haze.mp4"
            "MODERATE_HAZE" -> "haze.mp4"
            "HEAVY_HAZE" -> "haze.mp4"
            "LIGHT_RAIN" -> "rain.mp4"
            "MODERATE_RAIN" -> "rain.mp4"
            "HEAVY_RAIN" -> "rain.mp4"
            "STORM_RAIN" -> "thundershowers.mp4"
            "FOG" -> "fog.mp4"
            "LIGHT_SNOW" -> "snow.mp4"
            "MODERATE_SNOW" -> "snow.mp4"
            "HEAVY_SNOW" -> "snow.mp4"
            "STORM_SNOW" -> "snow.mp4"
            "DUST" -> "sand.mp4"
            "SAND" -> "sand.mp4"
            "WIND" -> "cloud.mp4"
            else -> throw RuntimeException("Unknown skyCon")
        }
    }

    fun getSkyConDescription(skyCon: String): String {
        return when (skyCon) {
            "CLEAR_DAY" -> "晴"
            "CLEAR_NIGHT" -> "晴"
            "PARTLY_CLOUDY_DAY" -> "多云"
            "PARTLY_CLOUDY_NIGHT" -> "多云"
            "CLOUDY" -> "多云"
            "LIGHT_HAZE" -> "轻雾霾"
            "MODERATE_HAZE" -> "中雾霾"
            "HEAVY_HAZE" -> "重雾霾"
            "LIGHT_RAIN" -> "小雨"
            "MODERATE_RAIN" -> "中雨"
            "HEAVY_RAIN" -> "大雨"
            "STORM_RAIN" -> "暴雨"
            "FOG" -> "雾"
            "LIGHT_SNOW" -> "小雪"
            "MODERATE_SNOW" -> "中雪"
            "HEAVY_SNOW" -> "大雪"
            "STORM_SNOW" -> "暴雪"
            "DUST" -> "浮尘"
            "SAND" -> "沙尘"
            "WIND" -> "大风"
            else -> throw RuntimeException("Unknown skyCon")
        }
    }

    fun getSkyConImage(skyCon: String): Int {
        return when (skyCon) {
            "CLEAR_DAY" -> R.drawable.ic_week_sun
            "CLEAR_NIGHT" -> R.drawable.ic_week_sun_night
            "PARTLY_CLOUDY_DAY" -> R.drawable.ic_week_cloudy
            "PARTLY_CLOUDY_NIGHT" -> R.drawable.ic_week_cloudy_night
            "CLOUDY" -> R.drawable.ic_week_cloudy
            "LIGHT_HAZE" -> R.drawable.ic_week_haze
            "MODERATE_HAZE" -> R.drawable.ic_week_haze
            "HEAVY_HAZE" -> R.drawable.ic_week_haze
            "LIGHT_RAIN" -> R.drawable.ic_week_light_rain
            "MODERATE_RAIN" -> R.drawable.ic_week_moderate_rain
            "HEAVY_RAIN" -> R.drawable.ic_week_heavy_rain
            "STORM_RAIN" -> R.drawable.ic_week_thunder_rain
            "FOG" -> R.drawable.ic_week_fog
            "LIGHT_SNOW" -> R.drawable.ic_week_light_snow
            "MODERATE_SNOW" -> R.drawable.ic_week_moderate_snow
            "HEAVY_SNOW" -> R.drawable.ic_week_moderate_snow
            "STORM_SNOW" -> R.drawable.ic_week_moderate_snow
            "DUST" -> R.drawable.ic_week_dust
            "SAND" -> R.drawable.ic_week_dust_storm
            "WIND" -> R.drawable.ic_week_cloudy
            else -> throw RuntimeException("Unknown skyCon")
        }
    }
}