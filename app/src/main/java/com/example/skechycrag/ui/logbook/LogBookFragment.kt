package com.example.skechycrag.ui.logbook

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
import com.example.skechycrag.databinding.FragmentLogBookBinding
import com.example.skechycrag.ui.constants.Constants.Companion.USERNAME
import com.example.skechycrag.ui.logbook.adapter.LogBookAdapter
import com.example.skechycrag.ui.routedetail.RouteDetailState
import com.example.skechycrag.ui.routedetail.adapter.DetailAdapter
import com.google.android.material.snackbar.Snackbar

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LogBookFragment : Fragment() {

    private var _binding: FragmentLogBookBinding? = null
    private val binding get() = _binding!!

    private val logBookViewModel: LogBookViewModel by viewModels()

    private lateinit var logBookAdapter: LogBookAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLogBookBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logBookAdapter = LogBookAdapter()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = logBookAdapter

        logBookViewModel.showLogBook(USERNAME)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                logBookViewModel.routeDetailState.collect { logBookRoutes ->
                    when (logBookRoutes) {
                        is LogBookDetailState.Error -> errorState()
                        LogBookDetailState.Loading -> loadingState()
                        LogBookDetailState.Start -> startState()
                        is LogBookDetailState.Success -> {
                            withContext(Dispatchers.Main) {
                                logBookAdapter.updateList(logBookRoutes.logBookRoutes)
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
