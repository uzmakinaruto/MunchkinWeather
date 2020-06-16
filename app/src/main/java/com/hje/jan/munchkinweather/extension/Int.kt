package com.hje.jan.munchkinweather.extension

import android.content.res.Resources

/**
 * dp to px
 * */
fun Int.dp(): Int {
    return (this * Resources.getSystem().displayMetrics.density + 0.5).toInt()
}

/**
 * px to dp
 * */
fun Int.px(): Int {
    return (this / Resources.getSystem().displayMetrics.density + 0.5).toInt()
}