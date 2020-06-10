package com.hje.jan.munchkinweather.extension

import android.content.res.Resources

fun Int.dp(): Int {
    return (this * Resources.getSystem().displayMetrics.density + 0.5).toInt()
}

fun Int.px(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}