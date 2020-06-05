package com.hje.jan.munchkinweather.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.hje.jan.munchkinweather.ui.viewmodel.ManagerLocationFragmentViewModel
import kotlinx.android.synthetic.main.fragment_manager_location.*
import kotlinx.android.synthetic.main.item_location_footer.*
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast

class ManagerLocationFragment : Fragment(), AMapLocationListener {

    lateinit var adapter: ManagerLocationAdapter
    lateinit var dragHelper: ItemTouchHelper
    lateinit var managerLocationActivity: ManagerLocationActivity
    val viewModel by lazy { ViewModelProvider(this).get(ManagerLocationFragmentViewModel::class.java) }
    var locationClient: AMapLocationClient? = null
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
            if (managerLocationActivity.viewModel.locations.size == 0) {
                alert {
                    title = "请先添加一个城市"
                    okButton {
                        "确定"
                    }
                }.show()
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
                progressBar.visibility = View.GONE
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
        progressBar.visibility = View.VISIBLE
    }

    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        progressBar.visibility = View.GONE
        if (aMapLocation != null && aMapLocation.errorCode == 0) {
            /**
             * 查找数据库,看之前是否有定位数据
             * 无定位数据,创建新的定位数据
             * 有定位数据,更新定位信息
             * 查询天气状况
             * 保存都数据库
             * 刷新界面
             * */
            managerLocationActivity.viewModel.setLocateLocation(aMapLocation)
        } else {
            toast("定位失败,请检查网络与定位开关")
        }
        locationClient?.stopLocation()
    }
}