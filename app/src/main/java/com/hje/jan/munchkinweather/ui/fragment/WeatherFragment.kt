package com.hje.jan.munchkinweather.ui.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hje.jan.munchkinweather.MunchkinWeatherApplication
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.extension.dp
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.ui.activity.ForecastActivity
import com.hje.jan.munchkinweather.ui.activity.WeatherActivity
import com.hje.jan.munchkinweather.ui.adapter.DailyAdapter
import com.hje.jan.munchkinweather.ui.adapter.HourlyAdapter
import com.hje.jan.munchkinweather.ui.viewmodel.WeatherFragmentViewModel
import com.hje.jan.munchkinweather.util.*
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.section_weather_detail.*
import kotlinx.android.synthetic.main.section_weather_forecast.*
import kotlinx.android.synthetic.main.titlebar_weather.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColorResource
import java.util.*

/**
 *具体显示每个地方的天气信息
 * */
class WeatherFragment : Fragment() {
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(WeatherFragmentViewModel::class.java) }
    lateinit var weatherActivity: WeatherActivity
    companion object {
        val TAG = "WeatherFragment"
        val SCROLL_TO_TOP = (200 + 20).dp()
        fun newInstance(location: LocationItemBean): WeatherFragment {
            val fragment = WeatherFragment()
            fragment.arguments = Bundle().apply {
                putParcelable("location", location)
            }
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.location = arguments?.getParcelable("location")!!
        Log.d(TAG, "onAttach-${viewModel.location.name}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate-${viewModel.location.name}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView-${viewModel.location.name}")
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated-${viewModel.location.name}")
        weatherActivity = activity as WeatherActivity
        weatherUiInit()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.location.realTime != null) {
            weatherActivity.videoView.start()
        }
        Log.d(TAG, "onResume-${viewModel.location.name}")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause-${viewModel.location.name}")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop-${viewModel.location.name}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy-${viewModel.location.name}")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach-${viewModel.location.name}")
    }



    private fun scrollHandler(scrollY: Int) {
        if (weatherActivity.isCurrentFragment(this)) {
            /**activity上的UI变化 只有此fragment为当前fragment时才更新*/
            //showTitleBar(scrollY)
            if (scrollY >= SCROLL_TO_TOP) {
                weatherActivity.titleBar.setBackgroundColor(Color.WHITE)
                weatherActivity.titleDivider.visibility = View.VISIBLE
                var tempPercentage = (scrollY - SCROLL_TO_TOP).toFloat() / weatherActivity.titleBar.height
                if (tempPercentage >= 1) tempPercentage = 1f
                weatherActivity.titleBarTempText.alpha = tempPercentage
                weatherActivity.titleBarTempText.translationX = 100 * (1 - tempPercentage)
            } else {
                weatherActivity.titleBar.setBackgroundColor(Color.TRANSPARENT)
                weatherActivity.titleDivider.visibility = View.GONE
                weatherActivity.titleBarTempText.alpha = 0f
            }
            weatherActivity.videoView.translationY = -scrollY.toFloat() / 2
            var alphaPercentage = scrollY.toFloat() / SCROLL_TO_TOP
            if (alphaPercentage > 1f) alphaPercentage = 1f
            weatherActivity.viewModel.clapBoardAlpha = alphaPercentage
            weatherActivity.clapBoard.alpha = weatherActivity.viewModel.clapBoardAlpha
            Log.d(TAG, "scrollY:${scrollY}")
            weatherActivity.viewModel.currentPosition = scrollY
        }
    }

    private fun weatherUiInit() {
        /**默认字体太粗 换一个字体*/
        tempText.typeface = Typeface.createFromAsset(activity!!.assets, "msyhl.ttc")
        scrollView.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            scrollHandler(scrollY)
        }
        viewModel.isRefresh.observe(viewLifecycleOwner, Observer { it ->
            val results = it.getOrNull()
            if (results != null) {
                for ((index, fragment) in weatherActivity.viewModel.fragments.withIndex()) {
                    if (fragment.isVisible) {
                        if (results[index]) {
                            fragment.refreshWeatherUI()
                            refreshLayout.finishRefresh(true)
                        } else {
                            refreshLayout.finishRefresh(false)
                        }
                    }
                }
            } else {
                refreshLayout.finishRefresh(false)
            }
        })

        hourlyAdapter = HourlyAdapter(viewModel.location.hourly)
        hourlyRV.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        hourlyRV.adapter = hourlyAdapter

        dailyAdapter = DailyAdapter(viewModel.location.daily)
        dailyRV.layoutManager = LinearLayoutManager(context)
        dailyRV.adapter = dailyAdapter

        aqiLayout.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                aqiLayout.alpha = 0.5f
            } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                aqiLayout.alpha = 1f
            }
            true
        }
        gotoForecast.setOnClickListener {
            weatherActivity.startActivity<ForecastActivity>("forecast" to viewModel.location.daily)
        }

        refreshLayout.setOnRefreshListener {
            //viewModel.refreshWeather(viewModel.location.lng, viewModel.location.lat)
            refreshWeathers()
        }
            refreshWeatherUI()
    }


    private fun refreshWeathers() {
        viewModel.refreshWeathers()
    }

    private fun refreshWeatherUI() {
        viewModel.location.hourly?.let {
            hourlyAdapter.refresh(it)
            weatherDesc.text = it.description
        }
        viewModel.location.daily?.let {
            dailyAdapter.refresh(it)
            clothesIndex.text = it.life_index.dressing[0].desc
            getColdIndex.text = it.life_index.coldRisk[0].desc
            washCarIndex.text = it.life_index.carWashing[0].desc
        }
        viewModel.location.realTime?.let {
            weatherInfo.visibility = View.VISIBLE
            setSkyConColor(it.skycon)
            degree.imageResource = getDegreeColor(it.skycon)
            skyConText.text = getSkyConDescription(it.skycon)
            tempText.text = it.temperature.toInt().toString()
            aqiStatus.text = it.airQuality.description.chn
            aqiValue.text = it.airQuality.aqi.chn.toString()
            windLevel.text = getWindLevel(it.wind.speed).toString()
            windDirection.text = getWindDirectionString(it.wind.direction)
            humidity.text = (it.humidity * 100).toInt().toString()
            apparentTemp.text = it.apparentTemperature.toInt().toString()
            pressure.text = (it.pressure / 100).toInt().toString()
            dayText.text = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
            dateText.text =
                "${Calendar.getInstance().get(Calendar.MONTH) + 1}月${Calendar.getInstance().get(
                    Calendar.DAY_OF_MONTH
                )}日"
            dayOfWeekText.text =
                getDayOfWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
            ultravioletIndex.text = it.lifeIndex.ultraviolet.desc
            comfortIndex.text = it.lifeIndex.comfort.desc
            visibilityIndex.text = "${it.visibility.toInt()}公里"
            viewModel.updateLocation()
            if (weatherActivity.isCurrentFragment(this)) {
                /**首个fragment需要在这里刷新,其他在viewpager切换时刷新*/
                weatherActivity.titleBarTempText.text = "${it.temperature.toInt()}°"
                weatherActivity.videoView.setVideoURI(
                    Uri.parse(
                        "android.resource://${weatherActivity.packageName}/${getVideoNameBySkyCon(
                            it.skycon
                        )}"
                    )
                )
                weatherActivity.videoView.start()
                scrollView.viewTreeObserver.addOnGlobalLayoutListener {
                    scrollTo(weatherActivity.viewModel.currentPosition)
                }
            }
        }
        refreshLayout.postDelayed({
            if (MunchkinWeatherApplication.isFirstEnter && weatherActivity.isCurrentFragment(this)) {
                MunchkinWeatherApplication.isFirstEnter = false
                refreshLayout.autoRefresh()
            }
        }, 200)
    }

    fun getCurrentTemp(): String {
        viewModel.location.realTime?.let {
            return "${it.temperature.toInt()}°"
        }
        return ""
    }

    fun getSkyCon(): String? {
        return viewModel.location.realTime?.skycon
    }

    fun scrollTo(scrollY: Int) {
        Log.d(TAG, "${viewModel.location.name} scrollTo $scrollY")
        scrollView?.scrollY = scrollY
    }

    fun getCurrentPosition(): Int {
        return scrollView?.scrollY ?: 0
    }

    private fun setSkyConColor(skyCon: String) {
        tempText.textColorResource = getSkyConColor(skyCon).first
        skyConText.textColorResource = getSkyConColor(skyCon).first
        divider.textColorResource = getSkyConColor(skyCon).first
        aqiStatus.textColorResource = getSkyConColor(skyCon).first
        aqiValue.textColorResource = getSkyConColor(skyCon).first
        right.textColorResource = getSkyConColor(skyCon).first
        yesterdayDiff.textColorResource = getSkyConColor(skyCon).first
        forecastText.textColorResource = getSkyConColor(skyCon).first
        forecastRight.textColorResource = getSkyConColor(skyCon).first
    }
}