package com.example.skechycrag.ui.logbook.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.skechycrag.databinding.FragmentLogBookBinding
import com.example.skechycrag.databinding.ItemLogbookRoutesBinding
import com.example.skechycrag.ui.model.UserRouteModel

class LogBookViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemLogbookRoutesBinding.bind(view)

    fun bind(userRouteModel: UserRouteModel){
        binding.textViewRouteName.text = userRouteModel.route_name
        binding.textViewGrade.text = userRouteModel.grade
        binding.textViewTrie.text = userRouteModel.tries

    }
}