package com.hje.jan.munchkinweather.util

import com.hje.jan.munchkinweather.R
import java.text.SimpleDateFormat
import java.util.*

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

    fun getVideoNameBySkyCon(skyCon: String): Int {
        return when (skyCon) {
            SKYCONS[0] -> R.raw.sun
            SKYCONS[1] -> R.raw.sun
            SKYCONS[2] -> R.raw.cloud
            SKYCONS[3] -> R.raw.cloud
            SKYCONS[4] -> R.raw.cloud
            SKYCONS[5] -> R.raw.haze
            SKYCONS[6] -> R.raw.haze
            SKYCONS[7] -> R.raw.haze
            SKYCONS[8] -> R.raw.rain
            SKYCONS[9] -> R.raw.rain
            SKYCONS[10] -> R.raw.rain
            SKYCONS[11] -> R.raw.thundershowers
            SKYCONS[12] -> R.raw.fog
            SKYCONS[13] -> R.raw.snow
            SKYCONS[14] -> R.raw.snow
            SKYCONS[15] -> R.raw.snow
            SKYCONS[16] -> R.raw.snow
            SKYCONS[17] -> R.raw.sand
            SKYCONS[18] -> R.raw.sand
            SKYCONS[19] -> R.raw.cloud
            else -> throw RuntimeException("Unknown skyCon")
        }
    }

    fun getSkyConDescription(skyCon: String?): String {
        return when (skyCon) {
            SKYCONS[0] -> "晴"
            SKYCONS[1] -> "晴"
            SKYCONS[2] -> "多云"
            SKYCONS[3] -> "多云"
            SKYCONS[4] -> "多云"
            SKYCONS[5] -> "轻雾霾"
            SKYCONS[6] -> "中雾霾"
            SKYCONS[7] -> "重雾霾"
            SKYCONS[8] -> "小雨"
            SKYCONS[9] -> "中雨"
            SKYCONS[10] -> "大雨"
            SKYCONS[11] -> "暴雨"
            SKYCONS[12] -> "雾"
            SKYCONS[13] -> "小雪"
            SKYCONS[14] -> "中雪"
            SKYCONS[15] -> "大雪"
            SKYCONS[16] -> "暴雪"
            SKYCONS[17] -> "浮尘"
            SKYCONS[18] -> "沙尘"
            SKYCONS[19] -> "大风"
            else -> "N/A"
        }
    }

    fun getSkyConImage(skyCon: String?): Int {
        return when (skyCon) {
            SKYCONS[0] -> R.drawable.ic_week_sun
            SKYCONS[1] -> R.drawable.ic_week_sun_night
            SKYCONS[2] -> R.drawable.ic_week_cloudy
            SKYCONS[3] -> R.drawable.ic_week_cloudy_night
            SKYCONS[4] -> R.drawable.ic_week_overcast
            SKYCONS[5] -> R.drawable.ic_week_haze
            SKYCONS[6] -> R.drawable.ic_week_haze
            SKYCONS[7] -> R.drawable.ic_week_haze
            SKYCONS[8] -> R.drawable.ic_week_light_rain
            SKYCONS[9] -> R.drawable.ic_week_moderate_rain
            SKYCONS[10] -> R.drawable.ic_week_heavy_rain
            SKYCONS[11] -> R.drawable.ic_week_thunder_rain
            SKYCONS[12] -> R.drawable.ic_week_fog
            SKYCONS[13] -> R.drawable.ic_week_light_snow
            SKYCONS[14] -> R.drawable.ic_week_moderate_snow
            SKYCONS[15] -> R.drawable.ic_week_moderate_snow
            SKYCONS[16] -> R.drawable.ic_week_moderate_snow
            SKYCONS[17] -> R.drawable.ic_week_dust
            SKYCONS[18] -> R.drawable.ic_week_dust_storm
            SKYCONS[19] -> R.drawable.ic_week_cloudy
            else -> R.drawable.ic_week_na
        }
    }

    fun getWeekString(position: Int): String {
        val calendar = Calendar.getInstance()
        val dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + position) % 7
        return when (position) {
            0 -> "今天"
            1 -> "明天"
            else -> when (dayOfWeek) {
                1 -> "周日"
                2 -> "周一"
                3 -> "周二"
                4 -> "周三"
                5 -> "周四"
                6 -> "周五"
                0 -> "周六"
                else -> throw RuntimeException("Unknown weekday")
            }
        }
    }

    fun getUpdateString(updateTime: String): String {
        return "${SimpleDateFormat(
            "hh:mm",
            Locale.getDefault()
        ).format(Date(updateTime.toLong()))} 更新"
    }

    /**
     * 北	N	0	348.76-11.25
    北东北	NNE	22.5	11.26-33.75
    东北	NE	45	33.76-56.25
    东东北	ENE	67.5	56.26-78.75
    东	E	90	78.76-101.25
    东东南	ESE	112.5	101.26-123.75
    东南	SE	135	123.76-146.25
    南东南	SSE	157.5	146.26-168.75
    南	S	180	168.76-191.25
    南西南	 SSW	202.5	191.26-213.75
    西南	SW	225	213.76-236.25
    西西南	WSW	247.5	236.26-258.75
    西	W	270	258.76-281.25
    西西北	WNW	295.5	281.26-303.75
    西北	NW	315	303.76-326.25
    北西北	NNW	337.5	326.26-348.75*/

    fun getWindDirectionString(windDirection: Double): String {
        return when (windDirection) {
            in 0.0..11.25 -> "北风"
            in 348.76..360.0 -> "北风"
            in 11.26..33.75 -> "北东北风"
            in 33.76..56.25 -> "东北风"
            in 56.26..78.75 -> "东东北风"
            in 78.76..101.25 -> "东风"
            in 101.26..123.75 -> "东东南风"
            in 123.76..146.25 -> "东南风"
            in 146.26..168.75 -> "南东南风"
            in 168.76..191.25 -> "南风"
            in 191.26..213.75 -> "南西南风"
            in 213.76..236.25 -> "西南风"
            in 236.26..258.75 -> "西西南风"
            in 258.76..281.25 -> "西风"
            in 281.26..303.75 -> "西西北风"
            in 303.76..326.25 -> "西北风"
            in 326.26..348.75 -> "北西北风"
            else -> throw RuntimeException("Error direction")
        }
    }

    /**
     *
     * 0级	<1	无风
    1级	1-5	微风徐徐
    2级	6-11	清风
    3级	12-19	树叶摇摆
    4级	20-28	树枝摇动
    5级	29-38	风力强劲
    6级	39-49	风力强劲
    7级	50-61	风力超强
    8级	62-74	狂风大作
    9级	75-88	狂风呼啸
    10级	89-102	暴风毁树
    11级	103-117	暴风毁树
    12级	118-133	飓风
    13级	134-149	台风
    14级	150-166	强台风
    15级	167-183	强台风
    16级	184-201	超强台风
    17级	202-220	超强台风*/

    fun getWindLevel(speed: Double): Int {
        return when (speed) {
            in 0.0..0.9 -> 0
            in 1.0..5.9 -> 1
            in 6.0..11.9 -> 2
            in 12.0..19.9 -> 3
            in 20.0..28.9 -> 4
            in 29.0..38.9 -> 5
            in 39.0..49.9 -> 6
            in 50.0..61.9 -> 7
            in 62.0..74.9 -> 8
            in 75.0..88.9 -> 9
            in 89.0..102.9 -> 10
            in 103.0..117.9 -> 11
            in 118.0..133.9 -> 12
            in 134.0..149.9 -> 13
            in 150.0..166.9 -> 14
            in 167.0..183.9 -> 15
            in 184.0..201.9 -> 16
            in 202.0..220.0 -> 17
            else -> throw RuntimeException("Error speed")
        }
    }

    fun getWindDesc(speed: Double): String {
        return when (speed) {
            in 0.0..0.9 -> "无风"
            in 1.0..5.9 -> "微风徐徐"
            in 6.0..11.9 -> "清风"
            in 12.0..19.9 -> "树叶摇摆"
            in 20.0..28.9 -> "树枝摇摆"
            in 29.0..38.9 -> "风力强劲"
            in 39.0..49.9 -> "风力强劲"
            in 50.0..61.9 -> "风力超强"
            in 62.0..74.9 -> "狂风大作"
            in 75.0..88.9 -> "狂风呼啸"
            in 89.0..102.9 -> "暴风毁树"
            in 103.0..117.9 -> "暴风毁树"
            in 118.0..133.9 -> "飓风"
            in 134.0..149.9 -> "台风"
            in 150.0..166.9 -> "强台风"
            in 167.0..183.9 -> "强台风"
            in 184.0..201.9 -> "超强台风"
            in 202.0..220.0 -> "超强台风"
            else -> throw RuntimeException("Error speed")
        }
    }

    fun getDayOfWeek(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            1 -> "星期日"
            2 -> "星期一"
            3 -> "星期二"
            4 -> "星期三"
            5 -> "星期四"
            6 -> "星期五"
            7 -> "星期六"
            else -> throw RuntimeException("Unknown day of week")
        }
    }

    fun getSkyConColor(skyCon: String): Pair<Int, Int> {
        return when (skyCon) {
            "CLEAR_DAY", "CLEAR_NIGHT" -> Pair(R.color.color_sunny_start, R.color.color_sunny_end)
            "PARTLY_CLOUDY_DAY", "PARTLY_CLOUDY_NIGHT", "CLOUDY", "WIND" -> Pair(
                R.color.color_cloudy_start,
                R.color.color_cloudy_end
            )
            "LIGHT_HAZE", "MODERATE_HAZE", "HEAVY_HAZE" -> Pair(
                R.color.color_rain_start,
                R.color.color_rain_end
            )
            "LIGHT_RAIN", "MODERATE_RAIN", "HEAVY_RAIN", "STORM_RAIN" -> Pair(
                R.color.color_rain_start,
                R.color.color_rain_end
            )
            "FOG" -> Pair(R.color.color_sunny_start, R.color.color_sunny_end)
            "LIGHT_SNOW", "MODERATE_SNOW", "HEAVY_SNOW", "STORM_SNOW" -> Pair(
                R.color.color_rain_start,
                R.color.color_rain_end
            )
            "DUST", "SAND" -> Pair(
                R.color.color_rain_start,
                R.color.color_rain_end
            )
            else -> Pair(
                R.color.color_rain_start,
                R.color.color_rain_end
            )
        }
    }

    fun getDegreeColor(skyCon: String): Int {
        return when (skyCon) {
            "CLEAR_DAY", "CLEAR_NIGHT" -> R.drawable.shape_degree_sunny
            "PARTLY_CLOUDY_DAY", "PARTLY_CLOUDY_NIGHT", "CLOUDY", "WIND" -> R.drawable.shape_degree_cloudy
            "LIGHT_HAZE", "MODERATE_HAZE", "HEAVY_HAZE" -> R.drawable.shape_degree_rain
            "LIGHT_RAIN", "MODERATE_RAIN", "HEAVY_RAIN", "STORM_RAIN" -> R.drawable.shape_degree_rain
            "FOG" -> R.drawable.shape_degree_rain
            "LIGHT_SNOW", "MODERATE_SNOW", "HEAVY_SNOW", "STORM_SNOW" -> R.drawable.shape_degree_cloudy
            "DUST", "SAND" -> R.drawable.shape_degree_rain
            else -> R.drawable.shape_degree_rain
        }
    }
