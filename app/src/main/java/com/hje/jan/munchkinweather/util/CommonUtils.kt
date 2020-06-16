package com.hje.jan.munchkinweather.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View

/**
 * 常用工具
 * */
private const val DOUBLE_CLICK_INTERVAL = 300
private var lastTimeStamp = 0L


/**
 * 防止双击
 * */
fun isAvoidedDoubleClick(): Boolean {
    val currentTimeStamp = System.currentTimeMillis()
    if (currentTimeStamp - lastTimeStamp >= DOUBLE_CLICK_INTERVAL) {
        lastTimeStamp = currentTimeStamp
        return true
    }
    return false
}

