package com.hje.jan.munchkinweather.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.model.DailyResponse

class ForecastChartView : LinearLayout {
    private var dailyResponse: DailyResponse? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setData(dailyResponse: DailyResponse?) {
        this.dailyResponse = dailyResponse
        removeAllViews()
        for (index in 0 until 15) {
            val item = View.inflate(context, R.layout.item_forecast_daily, null)
            addView(item)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

}