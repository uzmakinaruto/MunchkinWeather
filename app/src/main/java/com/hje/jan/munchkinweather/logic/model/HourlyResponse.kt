package com.hje.jan.munchkinweather.logic.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class HourlyResponse(
    val result: Result,
    val status: String
) {
    data class Result(
        /**综合预报自然语言描述*/
        @SerializedName("forecast_keypoint") val forecastKeyPoint: String,
        val hourly: Hourly
    )

    @Parcelize
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
        /**天气状态*/
        @SerializedName("skycon") val skyCon: List<Skycon>,
        /**状态*/
        val status: String,
        /**温度*/
        val temperature: List<Temperature>,
        /**能见度*/
        val visibility: List<Visibility>,
        /**风力与风向*/
        val wind: List<Wind>
    ) : Parcelable

    @Parcelize
    data class AirQuality(
        val aqi: List<Aqi>,
        val pm25: List<Pm25>
    ) : Parcelable

    @Parcelize
    data class Cloudrate(
        val datetime: String,
        val value: Double
    ) : Parcelable

    @Parcelize
    data class Dswrf(
        val datetime: String,
        val value: Double
    ) : Parcelable

    @Parcelize
    data class Humidity(
        val datetime: String,
        val value: Double
    ) : Parcelable

    @Parcelize
    data class Precipitation(
        val datetime: String,
        val value: Double
    ) : Parcelable

    @Parcelize
    data class Pressure(
        val datetime: String,
        val value: Double
    ) : Parcelable

    @Parcelize
    data class Skycon(
        val datetime: String,
        val value: String
    ) : Parcelable

    @Parcelize
    data class Temperature(
        val datetime: String,
        val value: Double
    ) : Parcelable

    @Parcelize
    data class Visibility(
        val datetime: String,
        val value: Double
    ) : Parcelable

    @Parcelize
    data class Wind(
        val datetime: String,
        val direction: Double,
        val speed: Double
    ) : Parcelable

    @Parcelize
    data class Aqi(
        val datetime: String,
        val value: Value
    ) : Parcelable

    @Parcelize
    data class Pm25(
        val datetime: String,
        val value: Float
    ) : Parcelable

    @Parcelize
    data class Value(
        val chn: Int,
        val usa: Int
    ) : Parcelable
}

