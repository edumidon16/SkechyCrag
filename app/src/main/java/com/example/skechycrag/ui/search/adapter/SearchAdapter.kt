package com.example.skechycrag.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skechycrag.R
import com.example.skechycrag.ui.model.CragModel

class SearchAdapter(
    var cragList: List<CragModel> = emptyList(),
    private val onItemSelected: (String) -> Unit //Add tambien la descripcion de la crag
) :
    RecyclerView.Adapter<SearchViewHolder>() {

    fun updateList(cragList: List<CragModel>) {
        this.cragList = cragList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SearchViewHolder(layoutInflater.inflate(R.layout.item_crag_info, parent, false))
    }

    override fun getItemCount(): Int {
        return cragList.size
    }

    override fun onBindViewHolder(viewHolder: SearchViewHolder, position: Int) {
        val item = cragList[position]
        viewHolder.bind(item, onItemSelected)
    }

}