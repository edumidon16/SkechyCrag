package com.example.skechycrag.ui.routedetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skechycrag.R
import com.example.skechycrag.databinding.FragmentRouteDetailBinding
import com.example.skechycrag.databinding.FragmentSearchBinding
import com.example.skechycrag.ui.routedetail.adapter.DetailAdapter
import com.example.skechycrag.ui.search.SearchDetailState
import com.example.skechycrag.ui.search.adapter.SearchAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RouteDetailFragment : Fragment() {

    private var _binding: FragmentRouteDetailBinding? = null
    private val binding get() = _binding!!

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Take the name of the crag selected
        arguments.let { bundle ->
            cragName = bundle?.getString("cragName")
        }

        binding.cragNameTextView.text = cragName


        routeDetailAdapter = DetailAdapter()
        binding.routesRecyclerView.setHasFixedSize(true)
        binding.routesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.routesRecyclerView.adapter = routeDetailAdapter

        if(cragName != null){
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
                            withContext(Dispatchers.Main){
                                routeDetailAdapter.updateList(cragInfo.cragInfo)
                                binding.progressBar.isVisible = false
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startState() {

    }

    private suspend fun loadingState() {
        withContext(Dispatchers.Main){
            binding.progressBar.isVisible = true
        }
    }

    private suspend fun errorState() {
        withContext(Dispatchers.Main){
            binding.progressBar.isVisible = false
        }
        Snackbar.make(requireView(), "Error: Crag not found", Snackbar.LENGTH_SHORT).show()
    }

}