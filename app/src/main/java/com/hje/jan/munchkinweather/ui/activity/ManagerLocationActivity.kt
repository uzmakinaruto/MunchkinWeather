package com.hje.jan.munchkinweather.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.model.LocationItemBean
import com.hje.jan.munchkinweather.ui.fragment.AddLocationFragment
import com.hje.jan.munchkinweather.ui.fragment.ManagerLocationFragment
import com.hje.jan.munchkinweather.util.WindowUtil

class ManagerLocationActivity : AppCompatActivity() {

    /*private val managerLocationFragment by lazy { ManagerLocationFragment.newInstance() }
    private val addLocationFragment: AddLocationFragment by lazy { AddLocationFragment.newInstance() }*/
    private val selectedLocations = mutableListOf<LocationItemBean>()
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

    fun setLocation(locations: List<LocationItemBean>) {
        for (location in locations) {
            if (location.isSelected && !selectedLocations.contains(location)) {
                selectedLocations.add(location)
            }
        }
    }

    fun getLocations(): MutableList<LocationItemBean> {
        return selectedLocations
    }
}