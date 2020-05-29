package com.hje.jan.munchkinweather.logic.model

object DefaultLocations {

    val CITIES = listOf<LocationItemBean>(
        LocationItemBean("上海市", "121.480539", "31.235929"),
        LocationItemBean("北京市", "116.413384", "39.910925"),
        LocationItemBean("杭州市", "120.215512", "30.253083"),
        LocationItemBean("南京市", "118.802422", "32.064653"),
        LocationItemBean("苏州市", "120.592412", "31.303564"),
        LocationItemBean("深圳市", "114.064552", "22.548457"),
        LocationItemBean("成都市", "104.081534", "30.655822"),
        LocationItemBean("重庆市", "116.413384", "39.910925"),
        LocationItemBean("天津市", "117.209523", "39.093668"),
        LocationItemBean("武汉市", "114.311582", "30.598467"),
        LocationItemBean("西安市", "108.946466", "34.347269"),
        LocationItemBean("广州市", "116.413384", "39.910925")
    )

    val VIEWS = listOf<LocationItemBean>(
        LocationItemBean("长城", "116.88039", "40.670529"),
        LocationItemBean("龙庆峡", "116.454839", "39.92827"),
        LocationItemBean("松山", "121.567929", "25.062957"),
        LocationItemBean("龙潭", "121.222337", "24.854035"),
        LocationItemBean("陶然亭", "116.386782", "39.884783"),
        LocationItemBean("青龙湖", "116.712813", "40.454583"),
        LocationItemBean("圣莲山", "115.737588", "39.847532"),
        LocationItemBean("周口店", "115.933421", "39.692208")
    )
}