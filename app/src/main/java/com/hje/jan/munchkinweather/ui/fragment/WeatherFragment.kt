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
import android.widget.TextView
import android.widget.VideoView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.extension.dp2px
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.ui.activity.WeatherActivity
import com.hje.jan.munchkinweather.ui.adapter.DailyAdapter
import com.hje.jan.munchkinweather.ui.adapter.HourlyAdapter
import com.hje.jan.munchkinweather.ui.viewmodel.WeatherFragmentViewModel
import com.hje.jan.munchkinweather.util.WeatherUtil
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.section_weather_detail.*
import kotlinx.android.synthetic.main.section_weather_forecast.*
import kotlinx.android.synthetic.main.titlebar_weather.*
import org.jetbrains.anko.support.v4.toast
import java.util.*


class WeatherFragment : Fragment() {
    var scrollToShowTitleBarDistance = 0
    lateinit var titleBar: View
    lateinit var titleBarTempText: TextView
    lateinit var videoView: VideoView
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(WeatherFragmentViewModel::class.java) }
    lateinit var weatherActivity: WeatherActivity
    companion object {
        val TAG = "WeatherFragment"
        val SCROLL_TO_TOP = (300 + 16).dp2px()
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

    private fun initActivityView() {
        titleBarTempText = weatherActivity.titleBarTempText
        titleBar = weatherActivity.titleBar
        titleBar.viewTreeObserver.addOnGlobalLayoutListener {
            scrollToShowTitleBarDistance = SCROLL_TO_TOP - titleBar.height
        }
        videoView = weatherActivity.videoView
    }

    private fun scrollHandler(scrollY: Int) {
        if (weatherActivity.isCurrentFragment(this)) {
            /**activity上的UI变化 只有此fragment为当前fragment时才更新*/
            showTitleBar(scrollY)
            videoView.translationY = -scrollY.toFloat() / 2
            if (scrollY > scrollToShowTitleBarDistance) {
                val diff2 = scrollY - (SCROLL_TO_TOP - titleBar.height)
                var tempPercentage = diff2.toFloat() / titleBar.height
                if (tempPercentage >= 1) tempPercentage = 1f
                titleBarTempText.alpha = tempPercentage
                titleBarTempText.translationX = 100 * (1 - tempPercentage)
            } else {
                titleBarTempText.alpha = 0f
            }
        }
        var diff1 = scrollY.toFloat() / SCROLL_TO_TOP
        if (diff1 > 1f) diff1 = 1f
        weatherInfo.setBackgroundColor((((diff1 * 0xFF).toInt() shl 24) or 0x00FFFFFF))
        Log.d(TAG, "scrollY:${scrollY}")
        weatherActivity.viewModel.currentPosition = scrollY
    }

    private fun weatherUiInit() {
        /**默认字体太粗 换一个字体*/
        tempText.typeface = Typeface.createFromAsset(activity!!.assets, "msyhl.ttc")
        initActivityView()
        scrollView.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            scrollHandler(scrollY)
        }
        viewModel.weatherLiveData.observe(viewLifecycleOwner, Observer { result ->
            swipeRefreshLayout.isRefreshing = false
            val weatherResponse = result.getOrNull()
            if (null != weatherResponse) {
                //Log.d("weatherResponse", weatherResponse.realtime.toString())
                //Log.d("weatherResponse", weatherResponse.daily.toString())
                Log.d(TAG, weatherResponse.hourly.toString())
                viewModel.dailyResult = weatherResponse.daily
                viewModel.hourResult = weatherResponse.hourly
                viewModel.realtimeResult = weatherResponse.realtime
                refreshWeatherUI()
            } else {
                toast(result.exceptionOrNull()?.localizedMessage ?: "Unknown Exception")
            }
        })
        hourlyAdapter = HourlyAdapter(viewModel.hourResult)
        hourlyRV.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        hourlyRV.adapter = hourlyAdapter

        dailyAdapter = DailyAdapter(viewModel.dailyResult)
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
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshWeather(viewModel.location.lng, viewModel.location.lat)
        }
        viewModel.refreshWeather(viewModel.location.lng, viewModel.location.lat)
    }


    private fun refreshWeatherUI() {
        viewModel.hourResult?.let {
            hourlyAdapter.refresh(it)
            weatherDesc.text = it.description
        }
        viewModel.dailyResult?.let {
            dailyAdapter.refresh(it)
            clothesIndex.text = it.life_index.dressing[0].desc
            getColdIndex.text = it.life_index.coldRisk[0].desc
            washCarIndex.text = it.life_index.carWashing[0].desc
        }
        viewModel.realtimeResult?.let {
            weatherInfo.visibility = View.VISIBLE
            skyConText.text = WeatherUtil.getSkyConDescription(it.skycon)
            tempText.text = it.temperature.toInt().toString()
            aqiStatus.text = it.airQuality.description.chn
            aqiValue.text = it.airQuality.aqi.chn.toString()
            windLevel.text = WeatherUtil.getWindLevel(it.wind.speed).toString()
            windDirection.text = WeatherUtil.getWindDirectionString(it.wind.direction)
            humidity.text = (it.humidity * 100).toInt().toString()
            apparentTemp.text = it.apparentTemperature.toInt().toString()
            pressure.text = (it.pressure / 100).toInt().toString()
            dayText.text = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
            dateText.text =
                "${Calendar.getInstance().get(Calendar.MONTH) + 1}月${Calendar.getInstance().get(
                    Calendar.DAY_OF_MONTH
                )}日"
            dayOfWeekText.text =
                WeatherUtil.getDayOfWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
            ultravioletIndex.text = it.lifeIndex.ultraviolet.desc
            comfortIndex.text = it.lifeIndex.comfort.desc
            visibilityIndex.text = "${it.visibility.toInt()}公里"
            viewModel.location.skyCon = it.skycon
            viewModel.location.temp = it.temperature.toInt()
            viewModel.updateLocation()
            if (weatherActivity.isCurrentFragment(this)) {
                /**首个fragment需要在这里刷新,其他在viewpager切换时刷新*/
                titleBarTempText.text = "${it.temperature.toInt()}°"
                videoView.setVideoURI(
                    Uri.parse(
                        "android.resource://${weatherActivity.packageName}/${WeatherUtil.getVideoNameBySkyCon(
                            it.skycon
                        )}"
                    )
                )
                videoView.start()
                scrollView.viewTreeObserver.addOnGlobalLayoutListener {
                    scrollTo(weatherActivity.viewModel.currentPosition)
                }
            }
        }
    }

    fun getCurrentTemp(): String {
        viewModel.realtimeResult?.let {
            return "${it.temperature.toInt()}°"
        }
        return ""
    }

    fun getSkyCon(): String? {
        return viewModel.realtimeResult?.skycon
    }

    fun scrollTo(scrollY: Int) {
        Log.d(TAG, "${viewModel.location.name} scrollTo $scrollY")
        scrollView?.scrollY = scrollY
    }

    fun getCurrentPosition(): Int {
        return scrollView?.scrollY ?: 0
    }

    private fun showTitleBar(scrollY: Int) {
        val isShowTitleBarBg = scrollY.toFloat() / scrollToShowTitleBarDistance >= 1f
        if (isShowTitleBarBg) titleBar.setBackgroundColor(Color.WHITE)
        else titleBar.setBackgroundColor(Color.TRANSPARENT)
        Log.d(TAG, "${viewModel.location.name}isShowTitleBarBg =${isShowTitleBarBg}")
    }
}