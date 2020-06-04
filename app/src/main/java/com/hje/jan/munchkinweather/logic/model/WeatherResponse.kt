package com.hje.jan.munchkinweather.logic.model


data class WeatherResponse(
    val realtime:RealtimeResponse.Realtime,
    val daily:DailyResponse.Daily,
    val hourly:HourlyResponse.Hourly
)