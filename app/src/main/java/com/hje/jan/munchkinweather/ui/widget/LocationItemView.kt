package com.hje.jan.munchkinweather.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.util.WeatherUtil
import kotlinx.android.synthetic.main.item_location.view.*
import org.jetbrains.anko.imageResource

class LocationItemView : RelativeLayout {
    fun bindData(location: LocationItemBean) {
        locationText.text = location.name
        skyConImage.imageResource = WeatherUtil.getSkyConImage(location.skyCon)
        if (null == location.temp) tempText.text = "N/A"
        else tempText.text = "${location.temp}℃"
        skyConText.text = WeatherUtil.getSkyConDescription(location.skyCon)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.item_location, this)
    }
}