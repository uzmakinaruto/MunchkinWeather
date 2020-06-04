package com.hje.jan.munchkinweather.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.ui.fragment.AddLocationFragment
import com.hje.jan.munchkinweather.ui.fragment.ManagerLocationFragment
import com.hje.jan.munchkinweather.ui.viewmodel.ManagerLocationActivityViewModel
import com.hje.jan.munchkinweather.util.WindowUtil

class ManagerLocationActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(ManagerLocationActivityViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_location)
        WindowUtil.showTransparentStatusBar(this)
        if (null == savedInstanceState) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ManagerLocationFragment.newInstance())
                .commit()
        }
    }

    fun startAddLocation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AddLocationFragment.newInstance())
            .addToBackStack(null).commit()
    }

}