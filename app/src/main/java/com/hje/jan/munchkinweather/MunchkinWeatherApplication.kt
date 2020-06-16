package com.hje.jan.munchkinweather

import android.app.Application
import android.content.Context
import android.util.Log
import com.hje.jan.munchkinweather.logic.Repository

/**
 * Application类 启动后首先执行
 * */
class MunchkinWeatherApplication : Application() {

    companion object {
        lateinit var context: Context
        var isFirstEnter = true
        /**彩云天气令牌*/
        const val TOKEN = "9BZyqvUN3hKyVYuz"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Log.d("BootTime", "" + System.currentTimeMillis())
        Repository.getDatabaseLocations()
    }
}