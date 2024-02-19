package com.example.skechycrag.ui.routedetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skechycrag.R
import com.example.skechycrag.ui.model.CragModel
import com.example.skechycrag.ui.model.RouteModel
import com.example.skechycrag.ui.model.UserRouteModel
import com.example.skechycrag.ui.search.adapter.SearchViewHolder
import javax.inject.Inject

class DetailAdapter(
    var routeList:List<RouteModel> = emptyList(),
    private val onItemSelected: (UserRouteModel) -> Unit,
    private val showInfoDialog: (RouteModel) -> Unit
):RecyclerView.Adapter<DetailViewHolder>() {

    fun updateList(routeList: List<RouteModel>) {
        this.routeList = routeList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DetailViewHolder(layoutInflater.inflate(R.layout.item_crag_detail, parent, false))
    }

    override fun getItemCount(): Int {
        return routeList.size
    }

    override fun onBindViewHolder(viewHolder: DetailViewHolder, position: Int) {
        viewHolder.bind(routeList[position], onItemSelected, showInfoDialog)
    }
}