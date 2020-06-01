package com.hje.jan.munchkinweather.ui.fragment

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
import com.hje.jan.munchkinweather.logic.model.DefaultLocations
import com.hje.jan.munchkinweather.logic.model.PlaceResponse
import com.hje.jan.munchkinweather.ui.activity.WeatherActivity
import com.hje.jan.munchkinweather.ui.adapter.DefaultLocationAdapter
import com.hje.jan.munchkinweather.ui.adapter.SearchPlaceAdapter
import com.hje.jan.munchkinweather.ui.adapter.SearchTextChangeAdapter
import com.hje.jan.munchkinweather.ui.viewmodel.AddLocationFragmentViewModel
import kotlinx.android.synthetic.main.fragment_add_locaton.*
import kotlinx.android.synthetic.main.section_default_location.*
import org.jetbrains.anko.startActivity

class AddLocationFragment : Fragment() {

    val viewModel by lazy {
        ViewModelProvider(this).get(
            AddLocationFragmentViewModel::class.java
        )
    }

    lateinit var defaultCityAdapter: DefaultLocationAdapter
    lateinit var defaultViewAdapter: DefaultLocationAdapter
    lateinit var searchAdapter: SearchPlaceAdapter

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
        viewModel.selectLocations.observe(viewLifecycleOwner, Observer { result ->
            viewModel.locations.clear()
            viewModel.locations.addAll(result)
            initDefaultRV()
        })
        viewModel.refreshLocations()
    }

    private fun initDefaultRV() {
        initDefaultLocations()
        val layoutManager1 = GridLayoutManager(context, 4)
        val layoutManager2 = GridLayoutManager(context, 4)
        defaultCityAdapter = DefaultLocationAdapter(defaultCities, this)
        defaultViewAdapter = DefaultLocationAdapter(defaultViews, this)
        defaultCitiesRV.layoutManager = layoutManager1
        defaultViewsRV.layoutManager = layoutManager2
        defaultCitiesRV.adapter = defaultCityAdapter
        defaultViewsRV.adapter = defaultViewAdapter
    }

    private fun initDefaultLocations() {
        val selected = viewModel.locations
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
        searchAdapter = SearchPlaceAdapter(viewModel.foundedPlaces)
        recyclerview.adapter = searchAdapter
        recyclerview.layoutManager = LinearLayoutManager(context)
        searchAdapter.onClickListener = {
            activity?.startActivity<WeatherActivity>("place" to it)
        }
    }

    private fun refreshList(places: List<PlaceResponse.Place>) {
        viewModel.foundedPlaces.clear()
        viewModel.foundedPlaces.addAll(places)
        searchAdapter.notifyDataSetChanged()
    }

}