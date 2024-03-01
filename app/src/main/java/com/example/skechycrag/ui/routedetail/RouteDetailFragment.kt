package com.example.skechycrag.ui.routedetail

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skechycrag.R
import com.example.skechycrag.databinding.FragmentRouteDetailBinding
import com.example.skechycrag.ui.model.MoreInfoRouteModel
import com.example.skechycrag.ui.model.RouteModel
import com.example.skechycrag.ui.model.UserRouteModel
import com.example.skechycrag.ui.routedetail.adapter.DetailAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@AndroidEntryPoint
class RouteDetailFragment : Fragment() {

    private var _binding: FragmentRouteDetailBinding? = null
    private val binding get() = _binding!!

    private var currentDialog: Dialog? = null
    private var currentInfoObserver: Observer<List<MoreInfoRouteModel>?>? = null
    private var currentGradeObserver: Observer<String>? = null

    //ViewModel
    private val routeDetailViewModel: RouteDetailViewModel by viewModels()

    //Adapter
    private lateinit var routeDetailAdapter: DetailAdapter

    private var cragName: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRouteDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Take the name of the crag selected
        arguments.let { bundle ->
            cragName = bundle?.getString("cragName")
        }

        binding.cragNameTextView.text = cragName


        routeDetailAdapter = DetailAdapter(
            onItemSelected = { userRouteModel ->
                addRouteLogBook(userRouteModel)
            },
            showInfoDialog = { userRouteModel ->
                showMoreInfoDialog(userRouteModel)
            }
        )
        binding.routesRecyclerView.setHasFixedSize(true)
        binding.routesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.routesRecyclerView.adapter = routeDetailAdapter


        if (cragName != null) {
            routeDetailViewModel.searchByName(cragName!!)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                routeDetailViewModel.routeDetailState.collect { cragInfo ->
                    when (cragInfo) {
                        is RouteDetailState.Error -> errorState()
                        RouteDetailState.Loading -> loadingState()
                        RouteDetailState.Start -> startState()
                        is RouteDetailState.Success -> {
                            withContext(Dispatchers.Main) {
                                routeDetailAdapter.updateList(cragInfo.cragInfo)
                                binding.progressBar.isVisible = false
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addRouteLogBook(route: UserRouteModel) {
        var completeRoute = UserRouteModel(
            crag_name = cragName!!,
            route_name = route.route_name,
            grade = route.grade,
            type = route.type,
            comment = route.comment,
            tries = route.tries
        )
        Snackbar.make(
            requireView(),
            "Route added",
            Snackbar.LENGTH_SHORT
        ).show()
        routeDetailViewModel.addRouteToLogBook(completeRoute)
    }

    private fun showMoreInfoDialog(route: RouteModel) {

        currentDialog?.dismiss()

        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.more_info_dialog, null)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(dialogView)

        // Set dialog size
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams

        val routeName = dialogView.findViewById<TextView>(R.id.textViewRouteName)
        val bookGrade = dialogView.findViewById<TextView>(R.id.textViewBookGrade)
        val communityGrade = dialogView.findViewById<TextView>(R.id.textViewCommunityGrade)
        val comments = dialogView.findViewById<TextView>(R.id.textViewComments)
        val alertButton = dialogView.findViewById<ImageButton>(R.id.alertButton)
        val nextCommentButton = dialogView.findViewById<Button>(R.id.addCommentButton)
        val createAlertButton = dialogView.findViewById<Button>(R.id.createAlertButton)

        // Fetch the data
        routeDetailViewModel.moreInfoRoute(route.route_name, route.grade)

        currentGradeObserver?.let { routeDetailViewModel.averageGrade.removeObserver(it) }

        routeDetailViewModel.moreInfoState.observe(viewLifecycleOwner) { moreInfoState ->
            when (moreInfoState) {
                is CommunityState.Start -> TODO()
                is CommunityState.Loading -> {
                    // Show loading indicator or prepare UI for new data
                    print("loading")
                }
                is CommunityState.Error -> {
                    // Handle error, e.g., show an error message
                    Snackbar.make(
                        requireView(),
                        "Error: Zero feedback on this route",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is CommunityState.Success -> {
                    if (moreInfoState.moreInfoList == null || moreInfoState.moreInfoList.isEmpty()) {
                        Snackbar.make(
                            requireView(),
                            "Error: Zero feedback on this route",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        var currentCommentIndex = 0
                        routeName.text = route.route_name
                        bookGrade.text = "Book Grade: " + route.grade

                        comments.text =
                            moreInfoState.moreInfoList[currentCommentIndex].username + ": " + moreInfoState.moreInfoList[currentCommentIndex].comment + " (" + moreInfoState.moreInfoList[currentCommentIndex].grade + ")"
                        // Set up the alert button to show the next comment on each click
                        nextCommentButton.setOnClickListener {
                            // Increment the index to point to the next comment, wrapping around if at the end
                            currentCommentIndex =
                                (currentCommentIndex + 1) % moreInfoState.moreInfoList.size
                            // Update the comments TextView with the next comment
                            comments.text =
                                moreInfoState.moreInfoList[currentCommentIndex].username + ": " + moreInfoState.moreInfoList[currentCommentIndex].comment + " (" + moreInfoState.moreInfoList[currentCommentIndex].grade + ")"
                        }
                        routeDetailViewModel.getCommunityGrade()
                        // Before showing the new dialog, dismiss the old one
                        currentDialog?.dismiss()
                        dialog.show()
                        // Update the reference to the current dialog
                        currentDialog = dialog
                        alertButton.setOnClickListener {
                            showAlertDialog(moreInfoState.moreInfoList)
                        }

                        createAlertButton.setOnClickListener {
                            showCreateAlertDialog(route.route_name)
                            dialog.dismiss()
                        }
                    }

                    val gradeObserver = Observer<String> { averageGrade ->
                        // Update the UI with the calculated average grade
                        communityGrade.text = "Community Grade: " + averageGrade
                    }
                    currentGradeObserver = gradeObserver
                    routeDetailViewModel.averageGrade.observe(viewLifecycleOwner, gradeObserver)

                }
            }
        }
    }

    private fun showCreateAlertDialog(routeName: String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.create_alert_dialog)

        val commentEditText = dialog.findViewById<EditText>(R.id.commentEditText)
        val dateTextView = dialog.findViewById<TextView>(R.id.dateTextView)
        val createAlertButton = dialog.findViewById<Button>(R.id.createAlertButton)
        val calendar = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val selectedDate =
                    String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year)
                dateTextView.text = selectedDate
            }

        dateTextView.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        dialog.show()

        createAlertButton.setOnClickListener {
            if (!commentEditText.text.isNullOrEmpty() && !dateTextView.text.isNullOrEmpty()) {
                val comment = commentEditText.text.toString().trim()
                val date = dateTextView.text.toString().trim()
                val combinedString = "$date - $comment"

                routeDetailViewModel.addAlert(combinedString, routeName)
                dialog.dismiss()

                Snackbar.make(
                    requireView(),
                    "Alert added",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(
                    requireView(),
                    "Enter all the data required",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun showAlertDialog(moreInfoList: List<MoreInfoRouteModel>) {
        var alertList = mutableListOf<String>()
        var currentAlertIndex = 0
        for (data in moreInfoList) {
            if (!data.alert.isNullOrEmpty() && data.alert != "") {
                val newAlert = data.username + ": " + data.alert
                alertList.add(newAlert)
            }
        }
        if (alertList.size > currentAlertIndex) {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.alert_dialog)

            val tvAlertMessage = dialog.findViewById<TextView>(R.id.tvAlertMessage)
            val tvAlertCount = dialog.findViewById<TextView>(R.id.tvAlertCount)

            tvAlertMessage.text = alertList[currentAlertIndex]
            currentAlertIndex++
            tvAlertCount.text = "Alert ${currentAlertIndex}/${alertList.size}"

            val btnNext = dialog.findViewById<Button>(R.id.btnNextAlert)
            btnNext.setOnClickListener {
                if (alertList.size > currentAlertIndex) {
                    tvAlertMessage.text = alertList[currentAlertIndex]
                    currentAlertIndex++
                    tvAlertCount.text = "Alert ${currentAlertIndex}/${alertList.size}"
                } else {
                    currentAlertIndex = 0
                    tvAlertMessage.text = alertList[currentAlertIndex]
                    currentAlertIndex++
                    tvAlertCount.text = "Alert ${currentAlertIndex}/${alertList.size}"
                }
            }

            // Handling the close button click
            val btnCloseAlert = dialog.findViewById<Button>(R.id.btnCloseAlert)
            btnCloseAlert.setOnClickListener {
                dialog.dismiss() // Close the dialog
            }
            dialog.show()
        } else {
            Snackbar.make(
                requireView(),
                "There are not any alert for this route",
                Snackbar.LENGTH_SHORT
            ).show()
        }

    }

    private fun startState() {

    }

    private suspend fun loadingState() {
        withContext(Dispatchers.Main) {
            binding.progressBar.isVisible = true
        }
    }

    private suspend fun errorState() {
        withContext(Dispatchers.Main) {
            binding.progressBar.isVisible = false
        }
        Snackbar.make(requireView(), "Error: Crag not found", Snackbar.LENGTH_SHORT).show()
    }


}


