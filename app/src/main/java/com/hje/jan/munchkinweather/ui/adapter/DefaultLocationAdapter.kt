package com.hje.jan.munchkinweather.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hje.jan.munchkinweather.R
import com.hje.jan.munchkinweather.logic.model.LocationItemBean
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColor

class DefaultLocationAdapter(private val locations: List<LocationItemBean>) :
    RecyclerView.Adapter<DefaultLocationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_default_location, parent, false)
        val holder = ViewHolder(root)
        holder.name.setOnClickListener {
            val location = locations[holder.adapterPosition]
            if (location.isSelected) {
                location.isSelected = false
                holder.name.textColor = Color.BLACK
                holder.name.setBackgroundResource(R.drawable.shape_default_location)
            } else {
                location.isSelected = true
                holder.name.textColor = Color.WHITE
                holder.name.setBackgroundResource(R.drawable.shape_default_location_selected)
            }
        }
        return holder
    }

    override fun getItemCount() = locations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locations[position]
        holder.name.text = location.name
    }
}