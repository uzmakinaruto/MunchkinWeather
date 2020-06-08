package com.hje.jan.munchkinweather

import android.app.Application
import android.content.Context
import com.hje.jan.munchkinweather.logic.Repository

class MunchkinWeatherApplication : Application() {

    companion object {
        lateinit var context: Context
        /**彩云天气令牌*/
        const val TOKEN = "9BZyqvUN3hKyVYuz"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}