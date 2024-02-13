package com.example.skechycrag.ui.routedetail.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.skechycrag.databinding.ItemCragDetailBinding
import com.example.skechycrag.databinding.ItemCragInfoBinding
import com.example.skechycrag.ui.model.RouteModel

class DetailViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemCragDetailBinding.bind(view)

    fun bind(route:RouteModel){
        binding.routeNameTextView.text = route.route_name
        binding.routeGradeTextView.text = route.grade
        binding.routeTypeTextView.text = route.type
    }
}