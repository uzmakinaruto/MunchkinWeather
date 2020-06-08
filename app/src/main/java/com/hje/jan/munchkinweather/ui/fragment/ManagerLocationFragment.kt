package com.hje.jan.munchkinweather.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.hje.jan.munchkinweather.MunchkinWeatherApplication
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.ui.activity.ManagerLocationActivity
import com.hje.jan.munchkinweather.ui.adapter.LocationMoveCallBack
import com.hje.jan.munchkinweather.ui.adapter.ManagerLocationAdapter
import kotlinx.android.synthetic.main.alert_dialog_add_location.view.*
import kotlinx.android.synthetic.main.fragment_manager_location.*
import kotlinx.android.synthetic.main.item_location_footer.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.toast

class ManagerLocationFragment : Fragment(), AMapLocationListener {

    lateinit var adapter: ManagerLocationAdapter
    lateinit var dragHelper: ItemTouchHelper
    lateinit var managerLocationActivity: ManagerLocationActivity
    //val viewModel by lazy { ViewModelProvider(this).get(ManagerLocationFragmentViewModel::class.java) }
    var locationClient: AMapLocationClient? = null
    var locatingDialog: AlertDialog? = null
    val dialogWidth by lazy {
        val metrics = DisplayMetrics()
        managerLocationActivity.windowManager.defaultDisplay.getMetrics(metrics)
        (metrics.widthPixels * 0.5).toInt()
    }

    companion object {
        const val TAG = "ManagerLocationFragment"
        fun newInstance(): ManagerLocationFragment {
            return ManagerLocationFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manager_location, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        managerLocationActivity = activity as ManagerLocationActivity
        toolbar.setNavigationOnClickListener {
            if (managerLocationActivity.viewModel.locations.size == 0 || (managerLocationActivity.viewModel.locations.size == 1 && !managerLocationActivity.viewModel.locations[0].isLocateEnable)
            ) {
                showAddLocationDialog()
            } else {
                managerLocationActivity.finish()
            }
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ManagerLocationAdapter(
            managerLocationActivity.viewModel.locations,
            managerLocationActivity
        )
        dragHelper = ItemTouchHelper(LocationMoveCallBack(adapter))
        adapter.setHelper(dragHelper)
        recyclerview.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recyclerview.adapter = adapter
        dragHelper.attachToRecyclerView(recyclerview)
        adapter.addLocationListener = {
            managerLocationActivity.startAddLocation()
        }
        adapter.startLocationListener = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        managerLocationActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 0)
                } else {
                    startLocation()
                }
            } else {
                startLocation()
            }
        }
        managerLocationActivity.viewModel.isLocate.observe(
            viewLifecycleOwner,
            Observer { result ->
                locatingDialog?.dismiss()
                if (!result) {
                    toast("请求天气失败,请检查网络是否开启")
                }
                managerLocationActivity.viewModel.getLocations()
            })
        managerLocationActivity.viewModel.locationsLiveData.observe(
            viewLifecycleOwner,
            Observer { locations ->
                managerLocationActivity.viewModel.locations.clear()
                managerLocationActivity.viewModel.locations.addAll(locations)
                if (locations.isEmpty()) {
                    val defaultLocateLocation = LocationItemBean("", "", "", isLocate = true)
                    managerLocationActivity.viewModel.locations.add(defaultLocateLocation)
                    managerLocationActivity.viewModel.addLocation(defaultLocateLocation)
                }
                adapter.notifyDataSetChanged()
                recyclerview.post {
                    if (recyclerview.height > recyclerview.computeVerticalScrollRange()) {
                        footer.visibility = View.GONE
                    } else {
                        footer.visibility = View.VISIBLE
                        addLocation.setOnClickListener {
                            managerLocationActivity.startAddLocation()
                        }
                    }
                }
            })
        managerLocationActivity.viewModel.getLocations()
    }

    override fun onPause() {
        super.onPause()/*
        for ((index, location) in managerLocationActivity.viewModel.locations.withIndex()) {
            location.position = index
            managerLocationActivity.viewModel.updateLocation(location)
        }*/
        /**只有两个以上地址才进行交换*/
        if (managerLocationActivity.viewModel.locations.size > 2) {
            /**定位地址不能交换*/
            for (index in 1 until managerLocationActivity.viewModel.locations.size) {
                val location = managerLocationActivity.viewModel.locations[index]
                location.position = index
                managerLocationActivity.viewModel.updateLocation(location)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocation()
        }
    }

    private fun startLocation() {
        if (null == locationClient) {
            locationClient = AMapLocationClient(MunchkinWeatherApplication.context).apply {
                setLocationListener(this@ManagerLocationFragment)
            }
        }
        locationClient?.startLocation()
        showLocatingDialog()
    }

    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        locatingDialog?.dismiss()
        if (aMapLocation != null && aMapLocation.errorCode == 0) {
            managerLocationActivity.viewModel.setLocateLocation(aMapLocation)
        } else {
            toast("定位失败,请检查网络与定位开关")
        }
        locationClient?.stopLocation()
    }


    /**设置对话框宽高、位置等属性需要在对话框show后才能设置*/
    private fun showAddLocationDialog() {
        val root = View.inflate(managerLocationActivity, R.layout.alert_dialog_add_location, null)
        val dialog =
            AlertDialog.Builder(managerLocationActivity)
                .setView(root).setCancelable(false).create()
        root.okBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
        val params = dialog.window?.attributes
        params?.y = 150
        params?.gravity = Gravity.BOTTOM
        dialog.window?.attributes = params
        dialog.window?.decorView?.backgroundColor = Color.TRANSPARENT
    }

    private fun showLocatingDialog() {
        if (null == locatingDialog) {
            val root =
                View.inflate(managerLocationActivity, R.layout.alery_dialog_locate, null)
            locatingDialog =
                AlertDialog.Builder(managerLocationActivity)
                    .setView(root).setCancelable(false).show()
            locatingDialog?.let {
                val params = it.window?.attributes
                params?.width = dialogWidth
                it.window?.attributes = params
                it.window?.decorView?.backgroundColor = Color.TRANSPARENT
            }
        } else {
            locatingDialog?.show()
        }
    }
}