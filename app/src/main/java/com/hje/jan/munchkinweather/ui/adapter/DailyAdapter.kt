package com.hje.jan.munchkinweather.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hje.jan.munchkinweather.logic.model.DailyResponse
import com.hje.jan.munchkinweather.ui.widget.DailyItemView

class DailyAdapter(private var dailyResult: DailyResponse.Daily?) :
    RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: DailyItemView) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(DailyItemView(parent.context))
        holder.itemView.setOnClickListener { }
        return holder
    }

    override fun getItemCount(): Int {
        return if (null == dailyResult) 0
        else 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dailyResult?.let {
            (holder.itemView as DailyItemView).bindData(it, position)
        }

    }

    fun refresh(dailyResult: DailyResponse.Daily) {
        this.dailyResult = dailyResult
        notifyDataSetChanged()
    }
}