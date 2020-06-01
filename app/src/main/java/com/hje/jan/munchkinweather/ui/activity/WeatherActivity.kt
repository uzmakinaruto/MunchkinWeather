package com.hje.jan.munchkinweather.ui.activity

import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.model.PlaceResponse
import com.hje.jan.munchkinweather.ui.adapter.WeatherViewPagerAdapter
import com.hje.jan.munchkinweather.ui.fragment.WeatherFragment
import com.hje.jan.munchkinweather.ui.viewmodel.WeatherActivityViewModel
import com.hje.jan.munchkinweather.util.WindowUtil
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.titlebar_weather.*

class WeatherActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(WeatherActivityViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        WindowUtil.showTransparentStatusBar(this)
        initViewPager()
        initVideoView()
    }

    private fun initViewPager() {
        viewModel.selectLocations.observe(this, Observer { locations ->
            viewModel.fragments.clear()
            for (location in locations) {
                val fragment = WeatherFragment.newInstance(location)
                viewModel.fragments.add(fragment)
            }
            viewPager.adapter?.notifyDataSetChanged()
            locationText.text = viewModel.fragments[viewPager.currentItem].location.name
        })
        viewPager.adapter = WeatherViewPagerAdapter(supportFragmentManager, viewModel.fragments)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                viewPager.setBackgroundColor((((positionOffset * 0xFF).toInt() shl 24) or 0x00FFFFFF))
                Log.d(
                    "onPageScrolled",
                    "" + (((positionOffset * 0xFF).toInt() shl 24) or 0x00FFFFFF)
                )
                //videoView.translationY = -positionOffsetPixels.toFloat()
            }

            override fun onPageSelected(position: Int) {
                locationText.text = viewModel.fragments[position].location.name
                titleBarTempText.text = viewModel.fragments[position].getCurrentTemp()
            }
        })
        viewModel.refreshLocations()
    }

    /**为了防止VideoView播放前显示黑色背景 需要在xml设置videoView背景为非透明色*/
    private fun initVideoView() {

        videoView.setOnCompletionListener {
            videoView.start()
        }
        videoView.setOnPreparedListener { mp ->
            mp?.setOnInfoListener { _, what, _ ->
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    videoView.setBackgroundColor(Color.TRANSPARENT)
                }
                true
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (!videoView.isPlaying)
            videoView.start()
    }

    override fun onPause() {
        super.onPause()
        if (videoView.isPlaying)
            videoView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }
}