package com.hje.jan.munchkinweather.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.ui.activity.ManagerLocationActivity
import com.hje.jan.munchkinweather.ui.adapter.LocationMoveCallBack
import com.hje.jan.munchkinweather.ui.adapter.ManagerLocationAdapter
import com.hje.jan.munchkinweather.ui.viewmodel.ManagerLocationFragmentViewModel
import kotlinx.android.synthetic.main.fragment_manager_location.*
import kotlinx.android.synthetic.main.item_location_footer.*

class ManagerLocationFragment : Fragment() {

    lateinit var adapter: ManagerLocationAdapter
    lateinit var dragHelper: ItemTouchHelper
    val viewModel by lazy { ViewModelProvider(this).get(ManagerLocationFragmentViewModel::class.java) }
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
        toolbar.setNavigationOnClickListener {
            activity?.finish()
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ManagerLocationAdapter(viewModel.locations, this)
        dragHelper = ItemTouchHelper(LocationMoveCallBack(adapter))
        adapter.setHelper(dragHelper)
        recyclerview.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recyclerview.adapter = adapter
        dragHelper.attachToRecyclerView(recyclerview)
        adapter.addLocationListener = {
            (activity as ManagerLocationActivity).startAddLocation()
        }
        viewModel.selectLocations.observe(viewLifecycleOwner, Observer {
            viewModel.locations.clear()
            viewModel.locations.addAll(it)
            adapter.notifyDataSetChanged()
            recyclerview.post {
                if (recyclerview.height > recyclerview.computeVerticalScrollRange()) {
                    footer.visibility = View.GONE
                } else {
                    footer.visibility = View.VISIBLE
                    addLocation.setOnClickListener {
                        (activity as ManagerLocationActivity).startAddLocation()
                    }
                }
            }
        })
        viewModel.refreshLocations()
    }
}