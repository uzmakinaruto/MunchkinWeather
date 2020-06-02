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
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.ui.adapter.WeatherViewPagerAdapter
import com.hje.jan.munchkinweather.ui.fragment.WeatherFragment
import com.hje.jan.munchkinweather.ui.viewmodel.WeatherActivityViewModel
import com.hje.jan.munchkinweather.util.WeatherUtil
import com.hje.jan.munchkinweather.util.WindowUtil
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.titlebar_weather.*
import org.jetbrains.anko.startActivity

class WeatherActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(WeatherActivityViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        WindowUtil.showTransparentStatusBar(this)
        initViewPager()
        initVideoView()
        initToolbar()
    }

    private fun initToolbar() {
        managerLocationBtn.setOnClickListener {
            startActivity<ManagerLocationActivity>()
        }
    }

    private fun initViewPager() {
        viewModel.selectLocations.observe(this, Observer { locations ->
            /**启动若没有记录的地址,跳到ManagerLocationActivity*/
            if (locations.size == 0) {
                startActivity<ManagerLocationActivity>()
            } else {
                viewModel.locations.clear()
                viewModel.locations.addAll(locations)
                viewModel.fragments.clear()
                for (location in viewModel.locations) {
                    val fragment = WeatherFragment.newInstance(location)
                    viewModel.fragments.add(fragment)
                }
                viewPager.adapter?.notifyDataSetChanged()
                locationText.text = locations[viewPager.currentItem].name
                val position = intent.getIntExtra("position", -1)
                if (position != -1) {
                    viewPager.currentItem = position
                }
                pageIndicatorView.selection = position
            }
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
                if (position == viewPager.currentItem) {
                    clapBoard.alpha = positionOffset
                    /**划向下一页*/
                } else {
                    /**划向上一页*/
                    clapBoard.alpha = 1 - positionOffset
                }
            }

            override fun onPageSelected(position: Int) {
                clapBoard.alpha = 0f
                locationText.text = viewModel.locations[position].name
                titleBarTempText.text = viewModel.fragments[position].getCurrentTemp()
                videoView.pause()
                viewModel.fragments[position].getSkyCon()?.let { skyCon ->
                    videoView.setVideoURI(
                        Uri.parse(
                            "android.resource://${packageName}/${WeatherUtil.getVideoNameBySkyCon(
                                skyCon
                            )}"
                        )
                    )
                    videoView.start()
                }
            }
        })

        viewModel.currentScrollY.observe(this, Observer { position ->
            Log.d("position", "position:${position}")
            for (fragment in viewModel.fragments) {
                fragment.scrollTo(position)
            }
        })
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
        viewModel.refreshLocations()
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

    fun getCurrentLocation(): LocationItemBean {
        return viewModel.locations[viewPager.currentItem]
    }
}