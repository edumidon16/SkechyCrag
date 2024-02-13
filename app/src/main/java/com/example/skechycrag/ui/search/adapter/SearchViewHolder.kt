package com.example.skechycrag.ui.search.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.skechycrag.databinding.ItemCragInfoBinding
import com.example.skechycrag.ui.model.CragModel

class SearchViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemCragInfoBinding.bind(view)

    fun bind(cragModel: CragModel,  onItemSelected: (String) -> Unit ){
        binding.tvCragName.text = cragModel.crag_name
        binding.tvNumberRoutes.text = cragModel.number_routes.toString()
        binding.root.setOnClickListener { onItemSelected(cragModel.crag_name) }
    }
}