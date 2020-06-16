package com.hje.jan.munchkinweather.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.model.DailyResponse
import com.hje.jan.munchkinweather.util.*
import kotlinx.android.synthetic.main.item_forecast_daily.view.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.imageResource

class ForecastChartView : LinearLayout {

    companion object {
        /** px/℃*/
        const val TEMP_INTERVAL = 20
    }

    lateinit var daily: DailyResponse.Daily
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    var avgMaxTemp = 0
    var avgMinTemp = 0
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        paint.style = Paint.Style.STROKE
        paint.textSize = 30f
    }

    /**设置数据,刷新界面*/
    fun setData(daily: DailyResponse.Daily) {
        this.daily = daily
        removeAllViews()
        var maxTotalTemp = 0
        var minTotalTemp = 0
        for (index in daily.temperature.indices) {
            initItemView(index)
            maxTotalTemp += daily.temperature[index].max.toInt()
            minTotalTemp += daily.temperature[index].min.toInt()
        }
        avgMaxTemp = maxTotalTemp / childCount
        avgMinTemp = minTotalTemp / childCount
        invalidate()
    }

    /**设置每个Item对应数据*/
    private fun initItemView(index: Int) {
        val item = View.inflate(context, R.layout.item_forecast_daily, null)
        if (index % 2 == 0)
            item.backgroundResource = R.drawable.shape_chart_even
        else
            item.backgroundResource = R.drawable.shape_chart_odd
        item.weekText.text = getWeekString(index)
        daily.temperature[index].date.split("T")[0].split("-").apply {
            item.dateText.text = "${get(1)}/${get(2)}"
        }
        item.daySkyConImage.imageResource =
            getSkyConImage(daily.skycon_08h_20h[index].value)
        item.daySkyConText.text =
            getSkyConDescription(daily.skycon_08h_20h[index].value)
        item.nightSkyConImage.imageResource =
            getSkyConImage(daily.skycon_20h_32h[index].value)
        item.nightSkyConText.text =
            getSkyConDescription(daily.skycon_20h_32h[index].value)
        item.windLevel.text = "${getWindLevel(daily.wind[index].avg.speed)}级"
        item.windDirection.text =
            getWindDirectionString(daily.wind[index].avg.direction)
        addView(item)
    }

    /**ViewGroup刷新后重新执行dispatchDraw*/
    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        drawCubicBezierLine(true, canvas)
        drawCubicBezierLine(false, canvas)
    }

    /**画三次贝塞尔曲线*/
    private fun drawCubicBezierLine(isMaxTemp: Boolean, canvas: Canvas?) {
        path.reset()
        if (isMaxTemp) {
            paint.color = Color.parseColor("#efb336")
        } else {
            paint.color = Color.parseColor("#1296db")
        }
        if (childCount > 2) {
            for (index in 0 until daily.temperature.size - 1) {
                val startX = (index + 0.5) * width / childCount
                val startY =
                    if (isMaxTemp) height / 2 - 100 + (daily.temperature[index].max - avgMaxTemp) * TEMP_INTERVAL
                    else height / 2 + 100 + (daily.temperature[index].min - avgMinTemp) * TEMP_INTERVAL
                val endX = (index + 1.5) * width / childCount
                val endY =
                    if (isMaxTemp)
                        height / 2 - 100 + (daily.temperature[index + 1].max - avgMaxTemp) * TEMP_INTERVAL
                    else
                        height / 2 + 100 + (daily.temperature[index + 1].min - avgMinTemp) * TEMP_INTERVAL
                /**两个控制点*/
                val cX = (startX + endX) / 2
                val c1Y = startY
                val c2Y = endY
                if (index == 0) path.moveTo(startX.toFloat(), startY.toFloat())
                path.cubicTo(
                    cX.toFloat(), c1Y.toFloat(), cX.toFloat(), c2Y.toFloat(), endX.toFloat(),
                    endY.toFloat()
                )
                paint.style = Paint.Style.FILL
                paint.strokeWidth = 1f
                val bound = Rect()
                var text: String
                if (isMaxTemp) {
                    text = "${daily.temperature[index].max.toInt()}°"
                    paint.getTextBounds(text, 0, text.length, bound)
                    canvas?.drawText(
                        text,
                        startX.toFloat() - bound.width() / 2,
                        startY.toFloat() - 20,
                        paint
                    )
                } else {
                    text = "${daily.temperature[index].min.toInt()}°"
                    paint.getTextBounds(text, 0, text.length, bound)
                    canvas?.drawText(
                        text,
                        startX.toFloat() - bound.width() / 2,
                        startY.toFloat() + 20 + paint.textSize,
                        paint
                    )
                }
            }
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 5f
            canvas?.drawPath(path, paint)
        }
    }
}