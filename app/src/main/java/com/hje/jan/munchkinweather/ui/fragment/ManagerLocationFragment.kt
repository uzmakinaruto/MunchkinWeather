package com.hje.jan.munchkinweather.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.model.LocationItemBean
import com.hje.jan.munchkinweather.ui.activity.ManagerLocationActivity
import com.hje.jan.munchkinweather.ui.adapter.LocationMoveCallBack
import com.hje.jan.munchkinweather.ui.adapter.ManagerLocationAdapter
import kotlinx.android.synthetic.main.fragment_manager_location.*

class ManagerLocationFragment : Fragment() {

    lateinit var adapter: ManagerLocationAdapter
    lateinit var dragHelper: ItemTouchHelper
    private val locations: MutableList<LocationItemBean> by lazy { mutableListOf<LocationItemBean>() }
    companion object {
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
        adapter = ManagerLocationAdapter(locations)
        dragHelper = ItemTouchHelper(LocationMoveCallBack(adapter))
        adapter.setHelper(dragHelper)
        recyclerview.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recyclerview.adapter = adapter
        dragHelper.attachToRecyclerView(recyclerview)
        adapter.addLocationListener = {
            (activity as ManagerLocationActivity).startAddLocation()
        }
    }

}