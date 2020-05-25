package com.hje.jan.munchkinweather.util

object AvoidDoubleClickUtil {
    private val CLICK_INTERVAL = 300
    private var lastTimeStamp = 0L
    fun isClickable(): Boolean {
        val currentTimeStamp = System.currentTimeMillis()
        if (currentTimeStamp - lastTimeStamp >= CLICK_INTERVAL) {
            lastTimeStamp = currentTimeStamp
            return true
        }
        return false
    }
}