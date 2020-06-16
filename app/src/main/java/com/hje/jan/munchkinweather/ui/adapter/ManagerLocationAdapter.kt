package com.hje.jan.munchkinweather.ui.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.database.LocationItemBean
import com.hje.jan.munchkinweather.ui.activity.ManagerLocationActivity
import com.hje.jan.munchkinweather.ui.widget.LocationItemView
import com.hje.jan.munchkinweather.util.getSkyConDescription
import com.hje.jan.munchkinweather.util.getSkyConImage
import com.hje.jan.munchkinweather.util.isAvoidedDoubleClick
import kotlinx.android.synthetic.main.item_location.view.*
import kotlinx.android.synthetic.main.item_location_footer.view.*
import kotlinx.android.synthetic.main.item_location_header.view.*
import org.jetbrains.anko.imageResource
import java.util.*


class ManagerLocationAdapter(
    private val locations: MutableList<LocationItemBean>,
    private val activity: ManagerLocationActivity
) :
    RecyclerView.Adapter<ManagerLocationAdapter.ViewHolder>() {

    private var helper: ItemTouchHelper? = null
    var addLocationListener: (() -> Unit)? = null
    var startLocationListener: (() -> Unit)? = null
    var deleteLocationListener:(()->Unit)? = null
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
                val header = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_location_header, parent, false)
                holder = ViewHolder(header)
                header.locateSwitch.setOnClickListener {
                    if (header.locateSwitch.isChecked) {
                        header.locateSwitch.isChecked = false
                        startLocationListener?.invoke()
                    } else {
                        locations[0].isLocateEnable = false
                        activity.viewModel.updateLocation(locations[0])
                        notifyDataSetChanged()
                    }
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
                    if (isAvoidedDoubleClick()) {
                        activity.viewModel.deleteLocation(locations[holder.adapterPosition])
                        notifyDataSetChanged()
                        deleteLocationListener?.invoke()
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
            locations.size -> TYPE_FOOTER
            else -> TYPE_ITEMS
        }
    }

    override fun getItemCount() = locations.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (locations.size > 0) {
            if (getItemViewType(position) == TYPE_ITEMS) {
                val location = locations[position]
                (holder.itemView as LocationItemView).bindData(location)
            } else if (getItemViewType(position) == TYPE_HEADER) {
                val location = locations[position]
                val header = holder.itemView
                if (location.isLocateEnable) {
                    header.weatherLayout.visibility = View.VISIBLE
                    header.location.text = location.name
                    //header.temp.text = "${location.temp}℃"
                    header.temp.text = "${location.realTime?.temperature?.toInt()}℃"
                    //header.scText.text = WeatherUtil.getSkyConDescription(location.skyCon)
                    header.scText.text = getSkyConDescription(location.realTime?.skycon)
                    //header.scImage.imageResource = WeatherUtil.getSkyConImage(location.skyCon)
                    header.scImage.imageResource =
                        getSkyConImage(location.realTime?.skycon)
                    header.locateSwitch.isChecked = true
                } else {
                    header.weatherLayout.visibility = View.GONE
                    header.location.text = "显示定位"
                    header.scImage.imageResource = R.drawable.ic_week_na
                    header.locateSwitch.isChecked = false
                }
            }
        }
    }

    fun onMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(locations, fromPosition, toPosition)
        locations[fromPosition].position = toPosition
        locations[toPosition].position = fromPosition
        activity.viewModel.updateLocation(locations[fromPosition])
        activity.viewModel.updateLocation(locations[toPosition])
        notifyItemMoved(fromPosition, toPosition)
    }

    fun setHelper(helper: ItemTouchHelper) {
        this.helper = helper
    }

}