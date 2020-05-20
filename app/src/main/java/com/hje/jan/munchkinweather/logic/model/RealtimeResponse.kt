package com.hje.jan.munchkinweather.logic.model

import com.google.gson.annotations.SerializedName

data class RealtimeResponse(
    val result: Result,
    val status: String
) {

    data class Result(
        val realtime: Realtime
    )

    data class Realtime(
        /**空气质量*/
        @SerializedName("air_quality") val airQuality: AirQuality,
        /**体感温度*/
        @SerializedName("apparent_temperature") val apparentTemperature: Double,
        /**云量*/
        @SerializedName("cloudrate") val cloud: Double,
        /**向下短波辐射通量*/
        val dswrf: Double,
        /**相对湿度*/
        val humidity: Double,
        /**生活指数*/
        @SerializedName("life_index") val lifeIndex: LifeIndex,
        /**降水*/
        val precipitation: Precipitation,
        /**气压*/
        val pressure: Double,
        /**主要天气现象*/
        val skycon: String,
        /**返回状态*/
        val status: String,
        /**温度*/
        val temperature: Double,
        /**能见度*/
        val visibility: Double,
        /**风*/
        val wind: Wind
    )

    data class AirQuality(
        /**空气质量指数*/
        val aqi: Aqi,
        /**一氧化碳，质量浓度值*/
        val co: Float,
        /**空气质量描述*/
        val description: Description,
        /**二氧化氮，质量浓度值*/
        val no2: Float,
        /**臭氧，质量浓度值*/
        val o3: Float,
        /**pm10，质量浓度值*/
        val pm10: Int,
        /**pm25，质量浓度值*/
        val pm25: Float,
        /**二氧化硫，质量浓度值*/
        val so2: Float
    )

    data class LifeIndex(
        /**舒适度指数及其自然语言描述*/
        val comfort: Comfort,
        /**紫外线指数及其自然语言描述*/
        val ultraviolet: Ultraviolet
    )

    data class Precipitation(
        /**降水*/
        val local: Local,
        val nearest: Nearest
    )

    data class Wind(
        /**风向，单位是度。正北方向为0度，顺时针增加到360度*/
        val direction: Double,
        /**风速，米制下是公里每小时*/
        val speed: Double
    )

    data class Aqi(
        /**中国标准*/
        val chn: Int,
        /**美国标准*/
        val usa: Int
    )

    data class Description(
        val chn: String,
        val usa: String
    )

    data class Comfort(
        /**舒适度指数及其自然语言描述*/
        val desc: String,
        val index: Int
    )

    data class Ultraviolet(
        /**紫外线指数及其自然语言描述*/
        val desc: String,
        val index: Double
    )

    data class Local(
        /**本地降水观测的数据源*/
        @SerializedName("datasource") val dataSource: String,
        /**本地降水强度（单位为雷达降水强度）*/
        val intensity: Double,
        val status: String
    )

    data class Nearest(
        /**最近的降水带距离*/
        val distance: Double,
        /**最近的降水带降水强度（单位为雷达降水强度）*/
        val intensity: Double,
        val status: String
    )
}
