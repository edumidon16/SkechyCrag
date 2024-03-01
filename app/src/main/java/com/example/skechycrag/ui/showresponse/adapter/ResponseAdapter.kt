package com.example.skechycrag.ui.showresponse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skechycrag.R
import com.example.skechycrag.ui.model.RouteInfo

class ResponseAdapter(
    private val routes: MutableList<RouteInfo>,
    private val onItemChanged: (RouteInfo) -> Unit
) : RecyclerView.Adapter<ResponseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_route, parent, false)
        return ResponseViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ResponseViewHolder, position: Int) {
        viewHolder.bind(routes[position], onItemChanged)
    }

    override fun getItemCount(): Int = routes.size

    // Function to update the list item at a given position
    fun updateItem(position: Int, routeInfo: RouteInfo) {
        routes[position] = routeInfo
        notifyItemChanged(position)
    }

}