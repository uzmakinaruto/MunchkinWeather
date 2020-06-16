package com.hje.jan.munchkinweather.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.fragment_manager_location.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.toast

class ManagerLocationFragment : Fragment(), AMapLocationListener {

    lateinit var adapter: ManagerLocationAdapter
    lateinit var dragHelper: ItemTouchHelper
    lateinit var managerLocationActivity: ManagerLocationActivity
    //val viewModel by lazy { ViewModelProvider(this).get(ManagerLocationFragmentViewModel::class.java) }
    var locationClient: AMapLocationClient? = null
    var locatingDialog: AlertDialog? = null
    private val dialogWidth by lazy {
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
            val locations = managerLocationActivity.viewModel.getLocations()
            if (locations.size == 0 || (locations.size == 1 && !locations[0].isLocateEnable)
            ) {
                managerLocationActivity.showAddLocationDialog()
            } else {
                managerLocationActivity.finish()
            }
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val locations = managerLocationActivity.viewModel.getLocations()
        if (locations.isEmpty()) {
            val defaultLocateLocation = LocationItemBean("", "", "", isLocate = true)
            managerLocationActivity.viewModel.addLocationWithoutRefresh(defaultLocateLocation)
        }
        adapter = ManagerLocationAdapter(
            locations,
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
        adapter.deleteLocationListener = {
            recyclerview.post {
                if (recyclerview.height > recyclerview.computeVerticalScrollRange()) {
                    footer.visibility = View.GONE
                } else {
                    footer.visibility = View.VISIBLE
                }
            }
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
                adapter.notifyDataSetChanged()
                //managerLocationActivity.viewModel.getLocations()
            })
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
        if (aMapLocation != null && aMapLocation.errorCode == 0) {
            managerLocationActivity.viewModel.getLocations()[0].name = aMapLocation.city
            managerLocationActivity.viewModel.getLocations()[0].lng =
                aMapLocation.longitude.toString()
            managerLocationActivity.viewModel.getLocations()[0].lat =
                aMapLocation.latitude.toString()
            managerLocationActivity.viewModel.updateLocation(managerLocationActivity.viewModel.getLocations()[0])
            managerLocationActivity.viewModel.getLocateWeatherInfo(managerLocationActivity.viewModel.getLocations()[0])
        } else {
            locatingDialog?.dismiss()
            toast("定位失败,请检查网络与定位开关")
        }
        locationClient?.stopLocation()
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