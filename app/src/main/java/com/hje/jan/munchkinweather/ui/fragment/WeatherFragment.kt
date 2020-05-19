package com.hje.jan.munchkinweather.ui.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.extension.dp2px
import com.hje.jan.munchkinweather.ui.activity.WeatherActivity
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.titlebar_weather.*


class WeatherFragment private constructor() : Fragment() {

    lateinit var name: String
    var scrollToShowTitleBarDistance = 0
    lateinit var titleBar: View
    lateinit var titleBarTempText: TextView

    companion object {
        val SCROLL_TO_TOP = (300 + 16).dp2px()
        val SCROLL_TO_SHOW_TITLE_BAR_TEMP_TEXT = 50.dp2px()

        fun newInstance(bgColor: Int, name: String): WeatherFragment {
            val fragment = WeatherFragment()
            fragment.arguments = Bundle().apply {
                putInt("bg_color", bgColor)
                putString("name", name)
            }
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        name = arguments?.getString("name", null) ?: "null"
        Log.d("MunchkinWeather", "onAttach-${name}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MunchkinWeather", "onCreate-${name}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MunchkinWeather", "onCreateView-${name}")
        val root = inflater.inflate(R.layout.fragment_weather, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("MunchkinWeather", "onActivityCreated-${name}")
        tempText.typeface = Typeface.createFromAsset(activity!!.assets, "msyhl.ttc")
        initTitleBar()
        scrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            val isShowTitleBarBg = scrollY.toFloat() / scrollToShowTitleBarDistance >= 1f
            var scrolled = scrollY.toFloat() / SCROLL_TO_TOP
            if (scrolled > 1f) scrolled = 1f
            if (isShowTitleBarBg) titleBar.setBackgroundColor(Color.WHITE)
            else titleBar.setBackgroundColor(Color.TRANSPARENT)
            bg.setBackgroundColor((((scrolled * 0xFF).toInt() shl 24) or 0x00FFFFFF))
            if (scrollToShowTitleBarDistance - scrollY < SCROLL_TO_SHOW_TITLE_BAR_TEMP_TEXT) {
                var tempAlpha =
                    1 - 0.8f * (scrollToShowTitleBarDistance - scrollY) / SCROLL_TO_SHOW_TITLE_BAR_TEMP_TEXT
                if (tempAlpha >= 1f) tempAlpha = 1f
                titleBarTempText.alpha = tempAlpha
                titleBarTempText.translationX = 100 - scrolled * 100
            } else {
                titleBarTempText.alpha = 0f
            }
            Log.d(
                "MunchkinWeather",
                "scrollY:${scrollY}  oldScrollY:${oldScrollY} scrolled:${scrolled}}"
            )

        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MunchkinWeather", "onResume-${name}")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MunchkinWeather", "onPause-${name}")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MunchkinWeather", "onStop-${name}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MunchkinWeather", "onDestroy-${name}")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("MunchkinWeather", "onDetach-${name}")
    }

    private fun initTitleBar() {
        titleBarTempText = (activity as WeatherActivity).titleBarTempText
        titleBar = (activity as WeatherActivity).titleBar
        titleBar.viewTreeObserver.addOnGlobalLayoutListener {
            scrollToShowTitleBarDistance = SCROLL_TO_TOP - titleBar.height
        }
    }
}