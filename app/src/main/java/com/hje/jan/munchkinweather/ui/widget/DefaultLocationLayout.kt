package com.hje.jan.munchkinweather.ui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.extension.dp2px
import com.hje.jan.munchkinweather.util.PreferenceUtils
import kotlinx.android.synthetic.main.default_location.view.*

class DefaultLocationLayout : RelativeLayout {


    private val cityList = listOf(
        LocationTag(text = "上海市"),
        LocationTag(text = "北京市"),
        LocationTag(text = "杭州市"),
        LocationTag(text = "南京市"),
        LocationTag(text = "苏州市"),
        LocationTag(text = "深圳市"),
        LocationTag(text = "成都市"),
        LocationTag(text = "重庆市"),
        LocationTag(text = "天津市"),
        LocationTag(text = "武汉市"),
        LocationTag(text = "西安市"),
        LocationTag(text = "广州市")
    )
    private val sightsList = listOf(
        LocationTag(text = "长城"),
        LocationTag(text = "龙庆峡"),
        LocationTag(text = "松山"),
        LocationTag(text = "龙潭"),
        LocationTag(text = "陶然亭"),
        LocationTag(text = "青龙湖"),
        LocationTag(text = "圣莲山"),
        LocationTag(text = "周口店")
    )

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.default_location, this)
        loadFromPreferences()
        initCityLayout()
        initSightsLayout()

    }

    private fun initCityLayout() {
        lateinit var childLinearLayout: LinearLayout
        val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.weight = 1f
        layoutParams.leftMargin = 4.dp2px()
        layoutParams.rightMargin = 4.dp2px()
        for (i in cityList.indices) {
            if (i % 4 == 0) {
                childLinearLayout = LinearLayout(context)
                childLinearLayout.orientation = LinearLayout.HORIZONTAL
                childLinearLayout.setPadding(16.dp2px(), 4.dp2px(), 16.dp2px(), 4.dp2px())
                famousCityLayout.addView(childLinearLayout)
            }
            val textView = TextView(context)
            textView.text = cityList[i].text
            textView.gravity = Gravity.CENTER
            if (cityList[i].isSelect) {
                textView.setTextColor(Color.WHITE)
                textView.setBackgroundResource(R.drawable.shape_default_location_selected)
            } else {
                textView.setTextColor(Color.BLACK)
                textView.setBackgroundResource(R.drawable.shape_default_location)
            }
            textView.setPadding(4.dp2px(), 4.dp2px(), 4.dp2px(), 4.dp2px())
            textView.layoutParams = layoutParams
            textView.isSingleLine = true
            textView.isClickable = true
            textView.setOnClickListener {
                cityList[i].isSelect = !cityList[i].isSelect
                if (cityList[i].isSelect) {
                    textView.setBackgroundResource(R.drawable.shape_default_location_selected)
                    textView.setTextColor(Color.WHITE)
                } else {
                    textView.setBackgroundResource(R.drawable.shape_default_location)
                    textView.setTextColor(Color.BLACK)
                }

            }
            childLinearLayout.addView(textView)
        }
    }

    private fun initSightsLayout() {
        lateinit var childLinearLayout: LinearLayout
        val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.weight = 1f
        layoutParams.leftMargin = 4.dp2px()
        layoutParams.rightMargin = 4.dp2px()
        for (i in sightsList.indices) {
            if (i % 4 == 0) {
                childLinearLayout = LinearLayout(context)
                childLinearLayout.orientation = LinearLayout.HORIZONTAL
                childLinearLayout.setPadding(16.dp2px(), 4.dp2px(), 16.dp2px(), 4.dp2px())
                famousSightsLayout.addView(childLinearLayout)
            }
            val textView = TextView(context)
            textView.text = sightsList[i].text
            textView.gravity = Gravity.CENTER
            if (sightsList[i].isSelect) {
                textView.setTextColor(Color.WHITE)
                textView.setBackgroundResource(R.drawable.shape_default_location_selected)
            } else {
                textView.setTextColor(Color.BLACK)
                textView.setBackgroundResource(R.drawable.shape_default_location)
            }
            textView.setPadding(4.dp2px(), 4.dp2px(), 4.dp2px(), 4.dp2px())
            textView.layoutParams = layoutParams
            textView.isSingleLine = true
            textView.setOnClickListener {
                sightsList[i].isSelect = !sightsList[i].isSelect
                if (sightsList[i].isSelect) {
                    textView.setBackgroundResource(R.drawable.shape_default_location_selected)
                    textView.setTextColor(Color.WHITE)
                } else {
                    textView.setBackgroundResource(R.drawable.shape_default_location)
                    textView.setTextColor(Color.BLACK)
                }
            }
            childLinearLayout.addView(textView)
        }
    }

    private fun loadFromPreferences() {
        val locations = PreferenceUtils.preference.getStringSet("locations", null)
        locations?.forEach OUTER@{ location ->
            cityList.forEach { city ->
                if (city.text == location) {
                    city.isSelect = true
                    return@OUTER
                }
            }

            sightsList.forEach { sights ->
                if (sights.text == location) {
                    sights.isSelect = true
                    return@OUTER
                }
            }
        }
    }

    private fun saveToPreferences(){
        val locations = HashSet<String>()
        cityList.forEach {
            if (it.isSelect)
                locations.add(it.text)
        }
        sightsList.forEach {
            if (it.isSelect)
                locations.add(it.text)
        }
        PreferenceUtils.putStringSet("locations", locations)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        /**返回时把选中的城市写入sharedPreferences*/
        saveToPreferences()
    }
}

data class LocationTag(
    var isSelect: Boolean = false,
    val text: String
)