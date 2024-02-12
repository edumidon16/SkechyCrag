package com.example.skechycrag.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skechycrag.databinding.FragmentSearchBinding
import com.example.skechycrag.ui.adapter.SearchAdapter
import com.example.skechycrag.ui.model.CragModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    //VIEWMODEL
    private val searchViewModel: SearchViewModel by viewModels()

    //ADAPTER
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewModel.searchByName(query.orEmpty())
                return false
            }

            override fun onQueryTextChange(newText: String?) = false
        })

        //Initiate adapter
        searchAdapter = SearchAdapter()
        binding.rvCragInfo.setHasFixedSize(true)
        binding.rvCragInfo.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCragInfo.adapter = searchAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.searchState.collect { cragInfo ->
                    when (cragInfo) {
                        is SearchDetailState.Error -> errorState()
                        SearchDetailState.Loading -> loadingState()
                        SearchDetailState.Start -> startState()
                        is SearchDetailState.Success -> {
                            withContext(Dispatchers.Main){
                                searchAdapter.updateList(cragInfo.cragInfo)
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

    private fun errorState() {
        Snackbar.make(requireView(), "Error: Crag not found", Snackbar.LENGTH_SHORT).show()
    }


}