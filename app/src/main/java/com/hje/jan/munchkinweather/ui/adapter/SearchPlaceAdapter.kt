package com.hje.jan.munchkinweather.ui.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hje.jan.munchkinweather.logic.model.Place
import com.hje.jan.munchkinweather.ui.widget.SearchPlaceItemView

class SearchPlaceAdapter(var places: List<Place>) :
    RecyclerView.Adapter<SearchPlaceAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: SearchPlaceItemView) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(SearchPlaceItemView(parent.context))
        holder.itemView.setOnClickListener {
            Log.d("setOnClickListener", "setOnClickListener: ${holder.adapterPosition}")
        }
        return holder
    }

    override fun getItemCount() = places.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]
        (holder.itemView as SearchPlaceItemView).bindView(place)
    }
}