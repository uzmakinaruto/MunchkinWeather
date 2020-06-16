package com.hje.jan.munchkinweather.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.model.DailyResponse
import com.hje.jan.munchkinweather.util.getSkyConDescription
import com.hje.jan.munchkinweather.util.getSkyConImage
import com.hje.jan.munchkinweather.util.getWeekString
import kotlinx.android.synthetic.main.item_daily.view.*
import org.jetbrains.anko.imageResource

class DailyItemView : RelativeLayout {
    fun bindData(dailyResult: DailyResponse.Daily, position: Int) {
        dayOfWeekText.text = getWeekString(position)
        dailyResult.temperature[position].date.split("T")[0].split("-").apply {
            dateText.text = "${get(1)}/${get(2)}"
        }
        skyConImage.imageResource = getSkyConImage(dailyResult.skyCon[position].value)
        skyConText.text = getSkyConDescription(dailyResult.skyCon[position].value)
        minTempText.text = "${dailyResult.temperature[position].min.toInt()}°"
        maxTempText.text = "${dailyResult.temperature[position].max.toInt()}°"
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.item_daily, this)
    }
}