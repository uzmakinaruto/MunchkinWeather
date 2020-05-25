package com.hje.jan.munchkinweather.ui.adapter

import android.graphics.Color
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.backgroundColor

class LocationMoveCallBack(
    val adapter: ManagerLocationAdapter
) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(ItemTouchHelper.DOWN or ItemTouchHelper.UP, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (adapter.getItemViewType(viewHolder.adapterPosition) != adapter.getItemViewType(target.adapterPosition)) {
            return false
        }
        adapter.onMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        viewHolder?.itemView?.backgroundColor = Color.WHITE
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }
}