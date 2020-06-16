package com.hje.jan.munchkinweather.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.ui.fragment.AddLocationFragment
import com.hje.jan.munchkinweather.ui.fragment.ManagerLocationFragment
import com.hje.jan.munchkinweather.ui.viewmodel.ManagerLocationActivityViewModel
import com.hje.jan.munchkinweather.util.showTransparentStatusBar
import kotlinx.android.synthetic.main.alert_dialog_add_location.view.*
import org.jetbrains.anko.backgroundColor

class ManagerLocationActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ManagerLocationActivity"
    }

    val viewModel by lazy { ViewModelProvider(this).get(ManagerLocationActivityViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_location)
        showTransparentStatusBar(this)
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


    /*override fun onBackPressed() {
        if (viewModel.locations.size == 1 && !viewModel.locations[0].isLocateEnable) {
            showAddLocationDialog()
        } else {
            finish()
        }
    }*/

    override fun onBackPressed() {
        val locations = viewModel.getLocations()
        if (locations.size == 1 && !locations[0].isLocateEnable) {
            showAddLocationDialog()
        } else {
            super.onBackPressed()
        }
    }

    /**设置对话框宽高、位置等属性需要在对话框show后才能设置*/
    fun showAddLocationDialog() {
        val root = View.inflate(this, R.layout.alert_dialog_add_location, null)
        val dialog =
            AlertDialog.Builder(this)
                .setView(root).setCancelable(false).create()
        root.okBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
        val params = dialog.window?.attributes
        params?.y = 150
        params?.gravity = Gravity.BOTTOM
        dialog.window?.attributes = params
        dialog.window?.decorView?.backgroundColor = Color.TRANSPARENT
    }

}