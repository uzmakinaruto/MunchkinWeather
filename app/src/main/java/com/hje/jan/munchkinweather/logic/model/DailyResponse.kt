package com.hje.jan.munchkinweather.logic.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
/**
 * 未来几天的天气信息
 * */
@Parcelize
data class DailyResponse(
    val result: Result,
    val status: String
) : Parcelable {
    @Parcelize
    data class Result(
        val daily: Daily
    ) : Parcelable

    @Parcelize
    data class Daily(
        /**空气质量*/
        @SerializedName("air_quality") val airQuality: AirQuality,
        /**日出与日落时刻*/
        val astro: List<Astro>,
        /**云量，最大值，平均值，最小值*/
        @SerializedName("cloudrate") val cloud: List<Cloudrate>,
        /**#短波辐射下向通量，最大值，平均值，最小值*/
        val dswrf: List<Dswrf>,
        /**#相对湿度，最大值，平均值，最小值*/
        val humidity: List<Humidity>,
        /**生活指数*/
        val life_index: LifeIndex,
        /**#降雨量，最大值，平均值，最小值*/
        val precipitation: List<Precipitation>,
        /**#气压，最大值，平均值，最小值*/
        val pressure: List<Pressure>,
        /**天气状态*/
        @SerializedName("skycon") val skyCon: List<Skycon>,
        /**白天天气状态*/
        val skycon_08h_20h: List<Skycon08h20h>,
        /**夜晚天气状态*/
        val skycon_20h_32h: List<Skycon20h32h>,
        /**返回状态*/
        val status: String,
        /**#温度，最大值，平均值，最小值*/
        val temperature: List<Temperature>,
        /** #能见度，最大值，平均值，最小值*/
        val visibility: List<Visibility>,
        /**#风力与风向，最大值，平均值，最小值*/
        val wind: List<Wind>
    ) : Parcelable

    @Parcelize
    data class AirQuality(
        /**空气质量指数最大值，平均值，最小值*/
        val aqi: List<Aqi>,
        /**PM25，最大值，平均值，最小值*/
        val pm25: List<Pm25>
    ) : Parcelable

    @Parcelize
    data class Astro(
        /**日出与日落时刻*/
        val date: String,
        val sunrise: Sunrise,
        val sunset: Sunset
    ) : Parcelable

    @Parcelize
    data class Cloudrate(
        /**云量，最大值，平均值，最小值*/
        val avg: Double,
        val date: String,
        val max: Double,
        val min: Double
    ) : Parcelable

    @Parcelize
    data class Dswrf(
        /**短波辐射下向通量，最大值，平均值，最小值*/
        val avg: Double,
        val date: String,
        val max: Double,
        val min: Double
    ) : Parcelable

    @Parcelize
    data class Humidity(
        /**相对湿度，最大值，平均值，最小值*/
        val avg: Double,
        val date: String,
        val max: Double,
        val min: Double
    ) : Parcelable

    @Parcelize
    data class LifeIndex(
        /**洗车指数*/
        val carWashing: List<CarWashing>,
        /**感冒指数*/
        val coldRisk: List<ColdRisk>,
        /**舒适度指数*/
        val comfort: List<Comfort>,
        /**穿衣指数*/
        val dressing: List<Dressing>,
        /**紫外线指数*/
        val ultraviolet: List<Ultraviolet>
    ) : Parcelable

    @Parcelize
    data class Precipitation(
        val avg: Double,
        val date: String,
        val max: Double,
        val min: Double
    ) : Parcelable

    @Parcelize
    data class Pressure(
        /**降水*/
        val avg: Double,
        val date: String,
        val max: Double,
        val min: Double
    ) : Parcelable

    @Parcelize
    data class Skycon(
        /**天气状态*/
        val date: String,
        val value: String
    ) : Parcelable

    @Parcelize
    data class Skycon08h20h(
        /**白天主要天气现象*/
        val date: String,
        val value: String
    ) : Parcelable

    @Parcelize
    data class Skycon20h32h(
        /**夜晚主要天气现象*/
        val date: String,
        val value: String
    ) : Parcelable

    @Parcelize
    data class Temperature(
        /**温度，最大值，平均值，最小值*/
        val avg: Double,
        val date: String,
        val max: Double,
        val min: Double
    ) : Parcelable

    @Parcelize
    data class Visibility(
        /**能见度，最大值，平均值，最小值*/
        val avg: Double,
        val date: String,
        val max: Double,
        val min: Double
    ) : Parcelable

    @Parcelize
    data class Wind(
        /**风力与风向，最大值，平均值，最小值*/
        val avg: AvgX,
        val date: String,
        val max: MaxX,
        val min: MinX
    ) : Parcelable

    @Parcelize
    data class Aqi(
        /**空气质量*/
        val avg: Avg,
        val date: String,
        val max: Max,
        val min: Min
    ) : Parcelable

    @Parcelize
    data class Pm25(
        /**PM25，最大值，平均值，最小值*/
        val avg: Float,
        val date: String,
        val max: Float,
        val min: Float
    ) : Parcelable

    @Parcelize
    data class Avg(
        val chn: Double,
        val usa: Double
    ) : Parcelable

    @Parcelize
    data class Max(
        val chn: Double,
        val usa: Double
    ) : Parcelable

    @Parcelize
    data class Min(
        val chn: Double,
        val usa: Double
    ) : Parcelable

    @Parcelize
    data class Sunrise(
        /**日出时间*/
        val time: String
    ) : Parcelable

    @Parcelize
    data class Sunset(
        /**日落时间*/
        val time: String
    ) : Parcelable

    @Parcelize
    data class CarWashing(
        /**洗车指数*/
        val date: String,
        val desc: String,
        val index: String
    ) : Parcelable

    @Parcelize
    data class ColdRisk(
        /**感冒指数*/
        val date: String,
        val desc: String,
        val index: String
    ) : Parcelable

    @Parcelize
    data class Comfort(
        /**舒适度指数*/
        val date: String,
        val desc: String,
        val index: String
    ) : Parcelable

    @Parcelize
    data class Dressing(
        /**穿衣指数*/
        val date: String,
        val desc: String,
        val index: String
    ) : Parcelable

    @Parcelize
    data class Ultraviolet(
        /**紫外线指数*/
        val date: String,
        val desc: String,
        val index: String
    ) : Parcelable

    @Parcelize
    /**风力与风向平均值 最大值 最小值*/
    data class AvgX(
        val direction: Double,
        val speed: Double
    ) : Parcelable

    @Parcelize
    data class MaxX(
        val direction: Double,
        val speed: Double
    ) : Parcelable

    @Parcelize
    data class MinX(
        val direction: Double,
        val speed: Double
    ) : Parcelable
}

