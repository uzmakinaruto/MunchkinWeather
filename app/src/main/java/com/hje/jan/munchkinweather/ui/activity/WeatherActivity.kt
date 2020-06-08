package com.hje.jan.munchkinweather.ui.activity

import android.app.Activity
import android.content.Intent
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
        initVideoView()
        initViewPager()
        initToolbar()
    }

    private fun initToolbar() {
        managerLocationBtn.setOnClickListener {
            startActivityForResult(Intent(this, ManagerLocationActivity::class.java), 0)
        }
    }

    private fun initViewPager() {
        viewModel.selectLocations.observe(this, Observer { locations ->
            /**启动若没有记录的地址,跳到ManagerLocationActivity*/
            if (locations.size == 0 || (locations.size == 1 && !locations[0].isLocateEnable)) {
                startActivity<ManagerLocationActivity>()
            } else {
                viewModel.locations.clear()
                viewModel.fragments.clear()
                for (location in locations) {
                    if (!location.isLocate || (location.isLocate && location.isLocateEnable)) {
                        viewModel.locations.add(location)
                        val fragment = WeatherFragment.newInstance(location)
                        viewModel.fragments.add(fragment)
                    }
                }
                viewPager.adapter?.notifyDataSetChanged()
                locationText.text = viewModel.locations[viewPager.currentItem].name
                viewPager.currentItem = viewModel.currentItem
                if(locations[0].isLocateEnable){
                    pageIndicatorView.setIsLocateEnable(true)
                }else{
                    pageIndicatorView.setIsLocateEnable(false)
                }
                pageIndicatorView.selection = viewModel.currentItem

            }
        })
        viewPager.offscreenPageLimit = 2
        viewPager.adapter = WeatherViewPagerAdapter(supportFragmentManager, viewModel.fragments)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                Log.d("StateChanged", "state:${state}")
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    val currentIndex = viewPager.currentItem
                    val position = viewModel.fragments[currentIndex].getCurrentPosition()
                    viewModel.currentPosition = position
                    /**把当前fragment的前一个后一个fragment滚动到相同位置*/
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
                if (position == viewPager.currentItem) {
                    clapBoard.alpha = positionOffset
                    /**划向下一页*/
                } else {
                    /**划向上一页*/
                    clapBoard.alpha = 1 - positionOffset
                }
            }

            override fun onPageSelected(position: Int) {
                viewModel.currentItem = position
                val fragment = viewModel.fragments[position]
                clapBoard.alpha = 0f
                locationText.text = viewModel.locations[position].name
                titleBarTempText.text = fragment.getCurrentTemp()
                videoView.pause()
                fragment.getSkyCon()?.let { skyCon ->
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
    }

    override fun onPause() {
        super.onPause()
        if (videoView.isPlaying) {
            videoView.pause()
        }
        videoView.setBackgroundColor(Color.WHITE)
        if (viewModel.locations.size > 0)
            viewModel.currentPosition =
                viewModel.fragments[viewPager.currentItem].getCurrentPosition()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }

    fun isCurrentFragment(fragment: WeatherFragment): Boolean {
        return viewModel.fragments[viewPager.currentItem] == fragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            viewModel.currentItem = data?.getIntExtra("position", 0) ?: 0
        }
    }
}