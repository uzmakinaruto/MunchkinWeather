package com.hje.jan.munchkinweather.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.model.DailyResponse
import kotlinx.android.synthetic.main.activity_forecast.*

class ForecastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        val dailyResponse = intent.getParcelableExtra<DailyResponse.Daily>("forecast")
        dailyResponse?.let {
            forecastChartView.setData(dailyResponse)
        }
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}