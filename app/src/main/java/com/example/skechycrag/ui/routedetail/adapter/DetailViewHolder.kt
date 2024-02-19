package com.example.skechycrag.ui.routedetail.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skechycrag.R
import com.example.skechycrag.databinding.ItemCragDetailBinding
import com.example.skechycrag.ui.model.RouteModel
import com.example.skechycrag.ui.model.UserRouteModel


class DetailViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemCragDetailBinding.bind(view)


    fun bind(route: RouteModel, onItemSelected: (UserRouteModel) -> Unit, showInfoDialog: (RouteModel) -> Unit) {
        binding.routeNameTextView.text = route.route_name
        //binding.routeTypeTextView.text = route.type
        binding.routeGradeTextView.text = route.grade
        binding.addLogBookButton.setOnClickListener {
            showAddDialog(route.route_name, route.type, onItemSelected)
        }
        binding.infoButton.setOnClickListener {
            showInfoDialog(route)
        }

    }

    private fun showAddDialog(
        routeName: String,
        type: String,
        onItemSelected: (UserRouteModel) -> Unit
    ) {
        val dialogView =
            LayoutInflater.from(itemView.context).inflate(R.layout.add_logbook_dialog, null)
        val dialog = Dialog(itemView.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(dialogView)

        // Set dialog size
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams

        // Find views in the dialog layout
        val routeNameTextView = dialogView.findViewById<TextView>(R.id.routeNameTextView)
        val addButton = dialogView.findViewById<Button>(R.id.addButton)
        val gradeEditText = dialogView.findViewById<EditText>(R.id.gradeEditText)
        val routeTypeTextView = dialogView.findViewById<TextView>(R.id.routeTypeTextView)
        val commentEditText = dialogView.findViewById<EditText>(R.id.commentEditText)
        val comboBox = dialogView.findViewById<Spinner>(R.id.comboBox)

        routeNameTextView.text = routeName
        routeTypeTextView.text = type

        addButton.setOnClickListener {
            var addRoute = UserRouteModel(
                crag_name = "",
                route_name = routeName,
                type = type,
                grade = gradeEditText.text.toString(),
                comment = commentEditText.text.toString(),
                tries = comboBox.selectedItem.toString()
            )
            onItemSelected(addRoute)
            dialog.dismiss()
        }
        dialog.show()
    }

}