package com.hje.jan.munchkinweather.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.logic.model.DefaultLocations
import com.hje.jan.munchkinweather.logic.model.PlaceResponse
import com.hje.jan.munchkinweather.ui.activity.ManagerLocationActivity
import com.hje.jan.munchkinweather.ui.adapter.DefaultLocationAdapter
import com.hje.jan.munchkinweather.ui.adapter.SearchPlaceAdapter
import com.hje.jan.munchkinweather.ui.adapter.SearchTextChangeAdapter
import com.hje.jan.munchkinweather.ui.viewmodel.AddLocationFragmentViewModel
import kotlinx.android.synthetic.main.fragment_add_locaton.*
import kotlinx.android.synthetic.main.section_default_location.*

class AddLocationFragment : Fragment() {

    val viewModel by lazy {
        ViewModelProvider(this).get(
            AddLocationFragmentViewModel::class.java
        )
    }

    lateinit var defaultCityAdapter: DefaultLocationAdapter
    lateinit var defaultViewAdapter: DefaultLocationAdapter
    lateinit var searchAdapter: SearchPlaceAdapter
    lateinit var managerLocationActivity: ManagerLocationActivity
    private val defaultCities = DefaultLocations.CITIES
    private val defaultViews = DefaultLocations.VIEWS

    companion object {

        fun newInstance(): AddLocationFragment {
            return AddLocationFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_locaton, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        managerLocationActivity = activity as ManagerLocationActivity
        initTitleBar()
        initSearchRV()
        viewModel.places.observe(viewLifecycleOwner, Observer { result ->
            /**搜索栏不为空时才显示搜索内容,不然搜索结果延迟会导致UI显示错乱*/
            if (!TextUtils.isEmpty(searchContent.text)) {
                val places = result.getOrNull()
                if (null == places || places.isEmpty()) {
                    recyclerview.visibility = View.GONE
                    layout_empty.visibility = View.VISIBLE
                } else {
                    refreshList(places)
                    recyclerview.visibility = View.VISIBLE
                    layout_empty.visibility = View.GONE
                }
            }
        })
        initDefaultRV()
    }

    private fun initDefaultRV() {
        initDefaultLocations()
        val layoutManager1 = GridLayoutManager(context, 4)
        val layoutManager2 = GridLayoutManager(context, 4)
        defaultCityAdapter = DefaultLocationAdapter(defaultCities, managerLocationActivity)
        defaultViewAdapter = DefaultLocationAdapter(defaultViews, managerLocationActivity)
        defaultCitiesRV.layoutManager = layoutManager1
        defaultViewsRV.layoutManager = layoutManager2
        defaultCitiesRV.adapter = defaultCityAdapter
        defaultViewsRV.adapter = defaultViewAdapter
    }

    private fun initDefaultLocations() {
        // val selected = managerLocationActivity.viewModel.locations
        val selected = managerLocationActivity.viewModel.locations
        defaultCities.forEach {
            it.isSelected = selected.contains(it)
        }
        defaultViews.forEach {
            it.isSelected = selected.contains(it)
        }
    }
    private fun initTitleBar() {
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        cancelBtn.setOnClickListener {
            searchContent.setText("")
        }
        searchContent.addTextChangedListener(object : SearchTextChangeAdapter {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s)) {
                    recyclerview.visibility = View.GONE
                    layout_empty.visibility = View.GONE
                    cancelBtn.visibility = View.GONE
                    default_location.visibility = View.VISIBLE
                } else {
                    default_location.visibility = View.GONE
                    cancelBtn.visibility = View.VISIBLE
                    viewModel.searchPlace(s.toString())
                }
            }
        })
    }

    private fun initSearchRV() {
        val locations = managerLocationActivity.viewModel.locations
        searchAdapter = SearchPlaceAdapter(viewModel.foundedPlaces)
        recyclerview.adapter = searchAdapter
        recyclerview.layoutManager = LinearLayoutManager(context)
        searchAdapter.onClickListener = {
            var position = 0
            val location = LocationItemBean(it.name, it.location.lng, it.location.lat)
            if (!locations.contains(location)) {
                position = locations.size
                location.position = position
                //添加location
                managerLocationActivity.viewModel.addLocation(location)
                //获取location天气信息
                managerLocationActivity.viewModel.getLocationWeatherInfo(location)
            } else {
                locations.forEach here@{ item ->
                    if (item == location) {
                        position = item.position
                        return@here
                    }
                }
            }
            if (!locations[0].isLocateEnable) position -= 1
            activity?.setResult(RESULT_OK, Intent().apply { putExtra("currentItem", position) })
            activity?.finish()
        }
    }

    private fun refreshList(places: List<PlaceResponse.Place>) {
        viewModel.foundedPlaces.clear()
        viewModel.foundedPlaces.addAll(places)
        searchAdapter.notifyDataSetChanged()
    }
    

}