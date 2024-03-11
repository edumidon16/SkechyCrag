package com.example.skechycrag.ui.showresponse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skechycrag.R
import com.example.skechycrag.databinding.FragmentAddBinding
import com.example.skechycrag.databinding.FragmentShowResponseBinding
import com.example.skechycrag.ui.model.RouteInfo
import com.example.skechycrag.ui.routedetail.adapter.DetailAdapter
import com.example.skechycrag.ui.showresponse.adapter.ResponseAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowResponseFragment : Fragment() {

    private var _binding: FragmentShowResponseBinding? = null
    private val binding get() = _binding!!

    //Adapter
    private lateinit var responseAdapter: ResponseAdapter

    private lateinit var newRoutesList :MutableList<RouteInfo>

    //ViewModel
    private val showResponseViewModel:ShowResponseViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowResponseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the Parcelable ArrayList and convert it to a MutableList
        val routeInfoList: MutableList<RouteInfo>? = arguments?.getParcelableArrayList<RouteInfo>("routeInfo")?.toMutableList()

        if(!routeInfoList.isNullOrEmpty()){
            newRoutesList = routeInfoList
            responseAdapter = ResponseAdapter(routeInfoList) { position, updateRouteInfo ->
                saveNewRoutes(position, updateRouteInfo)
            }
                binding.recyclerViewRoutes.setHasFixedSize(true)
                binding.recyclerViewRoutes.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerViewRoutes.adapter = responseAdapter
        }
        binding.saveButton.setOnClickListener {
            val cragName = binding.editTextCragName.text
            showResponseViewModel.addNewRoutes(cragName.toString(), newRoutesList)
        }

    }

    private fun saveNewRoutes(position: Int, newRoute: RouteInfo) {
        newRoutesList[position] = newRoute
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}