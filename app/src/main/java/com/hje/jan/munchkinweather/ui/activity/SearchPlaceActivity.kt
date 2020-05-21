package com.hje.jan.munchkinweather.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.model.PlaceResponse
import com.hje.jan.munchkinweather.ui.adapter.SearchPlaceAdapter
import com.hje.jan.munchkinweather.ui.adapter.SearchTextChangeAdapter
import com.hje.jan.munchkinweather.ui.viewmodel.PlaceActivityViewModel
import com.hje.jan.munchkinweather.util.WindowUtil
import kotlinx.android.synthetic.main.activity_search_place.*
import kotlinx.android.synthetic.main.titlebar_search.*
import org.jetbrains.anko.startActivity

class SearchPlaceActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(
            PlaceActivityViewModel::class.java
        )
    }
    lateinit var adapter: SearchPlaceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_place)
        WindowUtil.showTransparentStatusBar(this)
        initRecyclerView()
        initTitleBar()
        viewModel.places.observe(this, Observer { result ->
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

    }

    private fun initTitleBar() {
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

    private fun initRecyclerView() {
        adapter = SearchPlaceAdapter(viewModel.foundedPlaces)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter.onClickListener = {
            startActivity<WeatherActivity>("place" to it)
        }
    }

    private fun refreshList(places: List<PlaceResponse.Place>) {
        viewModel.foundedPlaces.clear()
        viewModel.foundedPlaces.addAll(places)
        adapter.notifyDataSetChanged()
    }
}