package com.hje.jan.munchkinweather.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View

object WindowUtil {
    fun showTransparentStatusBar(context: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val option =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            context.window.decorView.systemUiVisibility = option
            context.window.statusBarColor = Color.TRANSPARENT
        }
    }
}