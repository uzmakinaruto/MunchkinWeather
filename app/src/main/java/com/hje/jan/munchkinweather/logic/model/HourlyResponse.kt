package com.hje.jan.munchkinweather.logic.model

import com.google.gson.annotations.SerializedName

data class HourlyResponse(
    val result: Result,
    val status: String
) {
    data class Result(
        /**综合预报自然语言描述*/
        @SerializedName("forecast_keypoint") val forecastKeyPoint: String,
        val hourly: Hourly
    )

    data class Hourly(
        /**空气质量*/
        @SerializedName("air_quality") val airQuality: AirQuality,
        /**云量*/
        @SerializedName("cloudrate") val cloud: List<Cloudrate>,
        /**预报自然语言表述*/
        val description: String,
        /**短波辐射下向通量*/
        val dswrf: List<Dswrf>,
        /**相对湿度*/
        val humidity: List<Humidity>,
        /**降雨*/
        val precipitation: List<Precipitation>,
        /**气压*/
        val pressure: List<Pressure>,
        @SerializedName("skycon") val skyCon: List<Skycon>,
        /**状态*/
        val status: String,
        /**温度*/
        val temperature: List<Temperature>,
        /**能见度*/
        val visibility: List<Visibility>,
        /**风力与风向*/
        val wind: List<Wind>
    )

    data class AirQuality(
        val aqi: List<Aqi>,
        val pm25: List<Pm25>
    )

    data class Cloudrate(
        val datetime: String,
        val value: Double
    )

    data class Dswrf(
        val datetime: String,
        val value: Double
    )

    data class Humidity(
        val datetime: String,
        val value: Double
    )

    data class Precipitation(
        val datetime: String,
        val value: Double
    )

    data class Pressure(
        val datetime: String,
        val value: Double
    )

    data class Skycon(
        val datetime: String,
        val value: String
    )

    data class Temperature(
        val datetime: String,
        val value: Double
    )

    data class Visibility(
        val datetime: String,
        val value: Double
    )

    data class Wind(
        val datetime: String,
        val direction: Double,
        val speed: Double
    )

    data class Aqi(
        val datetime: String,
        val value: Value
    )

    data class Pm25(
        val datetime: String,
        val value: Int
    )

    data class Value(
        val chn: Int,
        val usa: Int
    )
}

