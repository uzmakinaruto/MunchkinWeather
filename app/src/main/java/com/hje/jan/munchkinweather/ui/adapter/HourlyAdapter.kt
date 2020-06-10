package com.hje.jan.munchkinweather.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hje.jan.munchkinweather.logic.model.HourlyResponse
import com.hje.jan.munchkinweather.ui.widget.HourlyItemView

class HourlyAdapter(private var hourlyResult: HourlyResponse.Hourly?) :
    RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: HourlyItemView) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(HourlyItemView(parent.context))
        holder.itemView.setOnClickListener {

        }
        return holder
    }

    override fun getItemCount(): Int {
        /**显示未来24小时预报*/
        return when {
            null == hourlyResult -> 0
            hourlyResult?.temperature?.size!! > 24 -> {
                return 24
            }
            else -> {
                return hourlyResult?.temperature?.size!!
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        hourlyResult?.let {
            (holder.itemView as HourlyItemView).bindData(it, position)
        }
    }

    fun refresh(hourlyResult: HourlyResponse.Hourly) {
        this.hourlyResult = hourlyResult
        notifyDataSetChanged()
    }
}