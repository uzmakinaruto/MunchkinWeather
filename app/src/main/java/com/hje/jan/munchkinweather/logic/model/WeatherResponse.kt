package com.hje.jan.munchkinweather.logic.model

/**
 * 所有天气信息的组合
 * */
data class WeatherResponse(
    val realtime:RealtimeResponse.Realtime,
    val daily:DailyResponse.Daily,
    val hourly:HourlyResponse.Hourly
)