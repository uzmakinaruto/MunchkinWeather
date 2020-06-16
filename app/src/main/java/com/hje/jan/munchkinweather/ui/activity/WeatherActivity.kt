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
import com.hje.jan.munchkinweather.util.getVideoNameBySkyCon
import com.hje.jan.munchkinweather.util.showTransparentStatusBar
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.titlebar_weather.*
import org.jetbrains.anko.startActivity

class WeatherActivity : AppCompatActivity() {

    companion object {
        const val TAG = "WeatherActivity"
    }
    val viewModel by lazy { ViewModelProvider(this).get(WeatherActivityViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        showTransparentStatusBar(this)
        initToolbar()
        initVideoView()
        initViewPager()
        viewModel.locations.observe(this, Observer { locations ->
            refreshViewPager(locations)
            Log.d("BootTime", "" + System.currentTimeMillis())
            Log.d("Observer-1", "" + locations.size)
        })
    }

    private fun initViewPager() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                Log.d("StateChanged", "state:${state}")
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    val currentIndex = viewPager.currentItem
                    val position = viewModel.fragments[currentIndex].getCurrentPosition()
                    viewModel.currentPosition = position
                    //把当前fragment的前一个后一个fragment滚动到相同位置
                    if (currentIndex > 0) {
                        viewModel.fragments[currentIndex - 1].scrollTo(position)
                    }
                    if (currentIndex < viewModel.fragments.size - 1) {
                        viewModel.fragments[currentIndex + 1].scrollTo(position)
                    }
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                var alpha: Float
                alpha = if (position == viewPager.currentItem) {
                    viewModel.clapBoardAlpha + positionOffset
                    //划向下一页
                } else {
                    //划向上一页
                    viewModel.clapBoardAlpha + 1 - positionOffset
                }
                if (alpha > 1) alpha = 1f
                Log.d(
                    TAG,
                    "alpha${alpha}  viewModel.clapBoardAlpha${viewModel.clapBoardAlpha}"
                )
                clapBoard.alpha = alpha
            }

            override fun onPageSelected(position: Int) {
                //viewModel.currentItem = position
                val fragment = viewModel.fragments[position]
                val locations = viewModel.locations.value!!
                clapBoard.alpha = 0f
                if (locations[0].isLocateEnable) {
                    locationText.text = locations[position].name
                } else {
                    locationText.text = locations[position + 1].name
                }
                titleBarTempText.text = fragment.getCurrentTemp()
                videoView.pause()
                fragment.getSkyCon()?.let { skyCon ->
                    videoView.setVideoURI(
                        Uri.parse(
                            "android.resource://${packageName}/${getVideoNameBySkyCon(
                                skyCon
                            )}"
                        )
                    )
                    videoView.start()
                }
            }
        })
        viewPager.adapter = WeatherViewPagerAdapter(supportFragmentManager, viewModel.fragments)
    }

    private fun initToolbar() {
        managerLocationBtn.setOnClickListener {
            startActivity<ManagerLocationActivity>()
        }
    }


    private fun refreshViewPager(locations: MutableList<LocationItemBean>) {
        if (locations.size == 0 || (locations.size == 1 && !locations[0].isLocateEnable)) {
            startActivity<ManagerLocationActivity>()
        } else {
            viewModel.fragments.clear()
            for (location in locations) {
                if (!location.isLocate || (location.isLocate && location.isLocateEnable)) {
                    val fragment = WeatherFragment.newInstance(location)
                    viewModel.fragments.add(fragment)
                }
            }
            viewPager.adapter?.notifyDataSetChanged()
            if (locations[0].isLocateEnable) {
                locationText.text = locations[viewPager.currentItem].name
                pageIndicatorView.setIsLocateEnable(true)
            } else {
                if (viewPager.currentItem == 0) {
                    locationText.text = locations[1].name
                } else {
                    locationText.text = locations[viewPager.currentItem].name
                }
                pageIndicatorView.setIsLocateEnable(false)
            }

        }
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
                    Log.d("stop", "stop")
                }
                true
            }
        }
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        if (videoView.isPlaying) {
            videoView.pause()
        }
        videoView.setBackgroundColor(Color.WHITE)
    }
    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }

    fun isCurrentFragment(fragment: WeatherFragment): Boolean {
        return viewModel.fragments[viewPager.currentItem] == fragment
    }

}