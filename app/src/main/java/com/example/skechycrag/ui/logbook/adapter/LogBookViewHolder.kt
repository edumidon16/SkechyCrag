package com.example.skechycrag.ui.logbook.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.skechycrag.R
import com.example.skechycrag.databinding.FragmentLogBookBinding
import com.example.skechycrag.databinding.ItemLogbookRoutesBinding
import com.example.skechycrag.ui.model.UserRouteModel

class LogBookViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemLogbookRoutesBinding.bind(view)

    fun bind(userRouteModel: UserRouteModel){
        binding.textViewCragName.text = userRouteModel.crag_name
        binding.textViewRouteName.text = userRouteModel.route_name
        binding.textViewGrade.text = userRouteModel.grade
        when (userRouteModel.tries) {
            "Onsight" -> {
                binding.textViewClimbingType.text = userRouteModel.tries
                binding.textViewClimbingType.setTextColor(ContextCompat.getColor(binding.textViewClimbingType.context, R.color.primaryColor))
            }
            "Flash" -> {
                binding.textViewClimbingType.text = userRouteModel.tries
                binding.textViewClimbingType.setTextColor(ContextCompat.getColor(binding.textViewClimbingType.context, R.color.blue))
            }
            "RedPoint" -> {
                binding.textViewClimbingType.text = userRouteModel.tries
                binding.textViewClimbingType.setTextColor(ContextCompat.getColor(binding.textViewClimbingType.context, R.color.red))
            }
        }


    }
}