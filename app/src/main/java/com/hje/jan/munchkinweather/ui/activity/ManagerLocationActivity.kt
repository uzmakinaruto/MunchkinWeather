package com.hje.jan.munchkinweather.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.ui.fragment.AddLocationFragment
import com.hje.jan.munchkinweather.ui.fragment.ManagerLocationFragment

class ManagerLocationActivity : AppCompatActivity() {

    val managerLocationFragment by lazy { ManagerLocationFragment.newInstance() }
    val addLocationFragment: AddLocationFragment by lazy { AddLocationFragment.newInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_location)
        supportFragmentManager.beginTransaction().replace(R.id.container, managerLocationFragment)
            .commit()
    }
}