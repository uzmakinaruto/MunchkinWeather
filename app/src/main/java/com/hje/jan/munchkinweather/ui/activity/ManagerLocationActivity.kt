package com.hje.jan.munchkinweather.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.ui.fragment.AddLocationFragment
import com.hje.jan.munchkinweather.ui.fragment.ManagerLocationFragment
import com.hje.jan.munchkinweather.util.WindowUtil

class ManagerLocationActivity : AppCompatActivity() {

    private val managerLocationFragment by lazy { ManagerLocationFragment.newInstance() }
    private val addLocationFragment: AddLocationFragment by lazy { AddLocationFragment.newInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_location)
        WindowUtil.showTransparentStatusBar(this)
        if (null == savedInstanceState) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, managerLocationFragment)
                .commit()
        }
    }

    fun startAddLocation() {
        supportFragmentManager.beginTransaction().replace(R.id.container, addLocationFragment)
            .addToBackStack(null).commit()
    }
}