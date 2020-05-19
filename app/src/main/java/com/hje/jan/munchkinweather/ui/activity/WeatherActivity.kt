package com.hje.jan.munchkinweather.ui.activity

import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.ui.adapter.WeatherViewPagerAdapter
import com.hje.jan.munchkinweather.ui.fragment.WeatherFragment
import com.hje.jan.munchkinweather.util.WindowUtil
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.titlebar_weather.*

class WeatherActivity : AppCompatActivity() {

    private val fragments = mutableListOf<WeatherFragment>()
    private val names = listOf<String>("Fragment1", "Fragment2", "Fragment3")
    private var titleHeight = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        WindowUtil.showTransparentStatusBar(this)
        initViewPager()
        initVideoView()
    }


    private fun initViewPager() {
        for (element in names) {
            fragments.add(WeatherFragment.newInstance(Color.WHITE, element))
        }
        viewPager.adapter = WeatherViewPagerAdapter(supportFragmentManager, fragments)
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
                videoView.translationY = -positionOffsetPixels.toFloat()
            }

            override fun onPageSelected(position: Int) {
            }
        })
    }

    private fun initVideoView() {
        videoView.setVideoURI(Uri.parse("android.resource://${packageName}/${R.raw.sun}"))
        videoView.setOnCompletionListener {
            videoView.start()
        }
        videoView.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(mp: MediaPlayer?) {
                mp?.setOnInfoListener { mp, what, extra ->
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        videoView.setBackgroundColor(Color.TRANSPARENT);
                    }
                    true
                }
            }
        })
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