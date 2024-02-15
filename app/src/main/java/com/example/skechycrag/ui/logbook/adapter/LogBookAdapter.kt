package com.example.skechycrag.ui.logbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skechycrag.R
import com.example.skechycrag.ui.model.RouteModel
import com.example.skechycrag.ui.model.UserRouteModel
import com.example.skechycrag.ui.routedetail.adapter.DetailViewHolder

class LogBookAdapter(
    var logBookList: List<UserRouteModel> = emptyList()
): RecyclerView.Adapter<LogBookViewHolder>() {

    fun updateList(logBookList: List<UserRouteModel>) {
        this.logBookList = logBookList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogBookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return LogBookViewHolder(layoutInflater.inflate(R.layout.item_logbook_routes, parent, false))
    }

    override fun getItemCount(): Int {
        return logBookList.size
    }

    override fun onBindViewHolder(viewHolder: LogBookViewHolder, position: Int) {
        viewHolder.bind(logBookList[position])
    }
}