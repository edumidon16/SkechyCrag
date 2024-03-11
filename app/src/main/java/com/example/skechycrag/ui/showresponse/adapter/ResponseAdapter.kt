package com.example.skechycrag.ui.showresponse.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.skechycrag.R
import com.example.skechycrag.ui.model.RouteInfo

class ResponseAdapter(
    private val routes: MutableList<RouteInfo>,
    private val onItemChanged: (Int, RouteInfo) -> Unit
) : RecyclerView.Adapter<ResponseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_route, parent, false)
        return ResponseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResponseViewHolder, position: Int) {
        val routeInfo = routes[position]
        holder.routeNameEditText.setText(routeInfo.routeName)
        holder.gradeEditText.setText(routeInfo.grade)

        holder.routeNameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val adapterPos = holder.adapterPosition
                if (adapterPos != RecyclerView.NO_POSITION) {
                    onItemChanged(adapterPos, routes[adapterPos].copy(routeName = s.toString()))
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        // Listener for grade changes
        holder.gradeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val adapterPos = holder.adapterPosition
                if (adapterPos != RecyclerView.NO_POSITION) {
                    onItemChanged(adapterPos, routes[adapterPos].copy(grade = s.toString()))
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    override fun getItemCount(): Int = routes.size
}