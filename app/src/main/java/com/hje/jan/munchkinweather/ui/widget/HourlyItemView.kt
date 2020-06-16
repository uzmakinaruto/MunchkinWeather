package com.hje.jan.munchkinweather.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.model.HourlyResponse
import com.hje.jan.munchkinweather.util.getSkyConColor
import com.hje.jan.munchkinweather.util.getSkyConDescription
import com.hje.jan.munchkinweather.util.getSkyConImage
import kotlinx.android.synthetic.main.item_hourly.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.textColorResource

class HourlyItemView : RelativeLayout {
    fun bindData(hourlyResult: HourlyResponse.Hourly, position: Int) {
        tempText.text = "${hourlyResult.temperature[position].value.toInt()}"
        tempText.textColorResource =
            getSkyConColor(hourlyResult.skyCon[0].value).first
        degree.textColorResource =
            getSkyConColor(hourlyResult.skyCon[position].value).first
        skyConText.text = getSkyConDescription(hourlyResult.skyCon[position].value)
        val timeString =
            hourlyResult.temperature[position].datetime
                .split("T")[1].split("+")[0]
        timeText.text = timeString
        skyConImage.imageResource = getSkyConImage(hourlyResult.skyCon[position].value)

    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.item_hourly, this)
    }
}