package com.example.skechycrag.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.skechycrag.databinding.ItemCragInfoBinding
import com.example.skechycrag.ui.model.CragModel

class SearchViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemCragInfoBinding.bind(view)

    fun bind(cragModel: CragModel){
        binding.tvCragName.text = cragModel.crag_name

    }
}