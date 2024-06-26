package com.example.skechycrag.ui.showresponse.adapter

import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.skechycrag.R
import com.example.skechycrag.databinding.ItemCragDetailBinding
import com.example.skechycrag.databinding.ItemRouteBinding
import com.example.skechycrag.ui.model.RouteInfo
import com.example.skechycrag.ui.model.RouteModel
import com.example.skechycrag.ui.model.UserRouteModel


class ResponseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val routeNameEditText: EditText = view.findViewById(R.id.editTextRouteName)
    val gradeEditText: EditText = view.findViewById(R.id.editTextGrade)
}