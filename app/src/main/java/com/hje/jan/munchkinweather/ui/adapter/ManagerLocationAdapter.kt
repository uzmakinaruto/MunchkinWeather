package com.hje.jan.munchkinweather.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.ui.fragment.ManagerLocationFragment
import com.hje.jan.munchkinweather.ui.widget.LocationHeaderView
import com.hje.jan.munchkinweather.ui.widget.LocationItemView
import com.hje.jan.munchkinweather.util.AvoidDoubleClickUtil
import kotlinx.android.synthetic.main.item_location.view.*
import kotlinx.android.synthetic.main.item_location_footer.view.*
import kotlinx.android.synthetic.main.item_location_header.view.*
import java.util.*


class ManagerLocationAdapter(
    private val locations: MutableList<LocationItemBean>,
    private val fragment: ManagerLocationFragment
) :
    RecyclerView.Adapter<ManagerLocationAdapter.ViewHolder>() {

    private var helper: ItemTouchHelper? = null
    var addLocationListener: (() -> Unit)? = null

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEMS = 1
        private const val TYPE_FOOTER = 2
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder: ViewHolder
        when (viewType) {
            TYPE_HEADER -> {
                val header = LocationHeaderView(parent.context)
                holder = ViewHolder(header)
                header.locateSwitch.setOnCheckedChangeListener { _, isChecked ->
                    Log.d("ManagerLocation", "isChecked $isChecked")
                }
            }
            TYPE_FOOTER -> {
                val footer = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_location_footer, parent, false)
                footer.addLocation.setOnClickListener {
                    addLocationListener?.invoke()
                }
                footer.addLocation.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        footer.addLocation.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100)
                            .start()
                    } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                        footer.addLocation.animate().scaleX(1f).scaleY(1f).start()
                    }
                    false
                }
                holder = ViewHolder(footer)
            }
            else -> {
                val itemView = LocationItemView(parent.context)
                holder = ViewHolder(itemView)
                itemView.removeLocation.setOnClickListener {
                    if (AvoidDoubleClickUtil.isClickable()) {
                       // locations.removeAt(holder.adapterPosition - 1)
                        fragment.viewModel.deleteLocation(locations[holder.adapterPosition - 1].name)
                    }
                }
                itemView.thumb.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        helper?.startDrag(holder)
                    }
                    true
                }
            }
        }
        return holder
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            locations.size + 1 -> TYPE_FOOTER
            else -> TYPE_ITEMS
        }
    }

    override fun getItemCount() = locations.size + 2

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_ITEMS) {
            val location = locations[position - 1]
            (holder.itemView as LocationItemView).bindData(location)
        }
    }

    fun onMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(locations, fromPosition - 1, toPosition - 1)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun setHelper(helper: ItemTouchHelper) {
        this.helper = helper
    }
}