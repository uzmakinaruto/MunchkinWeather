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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.extension.dp2px
import com.hje.jan.munchkinweather.logic.model.PlaceResponse
import com.hje.jan.munchkinweather.logic.model.WeatherResponse
import com.hje.jan.munchkinweather.ui.activity.WeatherActivity
import com.hje.jan.munchkinweather.ui.adapter.DailyAdapter
import com.hje.jan.munchkinweather.ui.adapter.HourlyAdapter
import com.hje.jan.munchkinweather.ui.viewmodel.WeatherFragmentViewModel
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.section_weather_forecast.*
import kotlinx.android.synthetic.main.titlebar_weather.*
import org.jetbrains.anko.support.v4.toast


class WeatherFragment : Fragment() {
    lateinit var place: PlaceResponse.Place
    var scrollToShowTitleBarDistance = 0
    lateinit var titleBar: View
    lateinit var titleBarTempText: TextView
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(WeatherFragmentViewModel::class.java) }
    companion object {
        val SCROLL_TO_TOP = (300 + 16).dp2px()
        fun newInstance(place: PlaceResponse.Place): WeatherFragment {
            val fragment = WeatherFragment()
            fragment.arguments = Bundle().apply {
                putParcelable("place", place)
            }
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        place = arguments?.getParcelable("place")!!
        Log.d("MunchkinWeather", "onAttach-${place.name}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MunchkinWeather", "onCreate-${place.name}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MunchkinWeather", "onCreateView-${place.name}")
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("MunchkinWeather", "onActivityCreated-${place.name}")
        weatherUiInit()
    }

    override fun onResume() {
        super.onResume()
        Log.d("MunchkinWeather", "onResume-${place.name}")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MunchkinWeather", "onPause-${place.name}")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MunchkinWeather", "onStop-${place.name}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MunchkinWeather", "onDestroy-${place.name}")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("MunchkinWeather", "onDetach-${place.name}")
    }

    private fun initTitleBar() {
        titleBarTempText = (activity as WeatherActivity).titleBarTempText
        titleBar = (activity as WeatherActivity).titleBar
        titleBar.viewTreeObserver.addOnGlobalLayoutListener {
            scrollToShowTitleBarDistance = SCROLL_TO_TOP - titleBar.height
        }
    }

    private fun scrollHandler(scrollY: Int) {
        val isShowTitleBarBg = scrollY.toFloat() / scrollToShowTitleBarDistance >= 1f
        var diff1 = scrollY.toFloat() / SCROLL_TO_TOP
        if (diff1 > 1f) diff1 = 1f
        if (isShowTitleBarBg) titleBar.setBackgroundColor(Color.WHITE)
        else titleBar.setBackgroundColor(Color.TRANSPARENT)
        bg.setBackgroundColor((((diff1 * 0xFF).toInt() shl 24) or 0x00FFFFFF))
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

    private fun weatherUiInit() {
        /**默认字体太粗 换一个字体*/
        tempText.typeface = Typeface.createFromAsset(activity!!.assets, "msyhl.ttc")
        initTitleBar()
        scrollView.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            scrollHandler(scrollY)
        }
        viewModel.weatherLiveData.observe(viewLifecycleOwner, Observer { result ->
            val weatherResponse = result.getOrNull()
            if (null != weatherResponse) {
                //Log.d("weatherResponse", weatherResponse.realtime.toString())
                //Log.d("weatherResponse", weatherResponse.daily.toString())
                Log.d("weatherResponse", weatherResponse.hourly.toString())
                viewModel.dailyResult = weatherResponse.daily
                viewModel.hourResult = weatherResponse.hourly
                refreshWeatherUI(weatherResponse)
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
        viewModel.refreshWeather(place.location.lng, place.location.lat)
    }

    private fun refreshWeatherUI(weatherResponse: WeatherResponse) {
        bg.visibility = View.VISIBLE
        tempText.text = weatherResponse.realtime.temperature.toInt().toString()
        titleBarTempText.text = "${weatherResponse.realtime.temperature.toInt()}°"
        viewModel.hourResult?.let { hourlyAdapter.refresh(it) }
        viewModel.dailyResult?.let { dailyAdapter.refresh(it) }
    }
}