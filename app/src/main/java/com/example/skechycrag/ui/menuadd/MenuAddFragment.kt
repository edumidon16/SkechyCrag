package com.example.skechycrag.ui.menuadd

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.skechycrag.R
import com.example.skechycrag.databinding.FragmentAddBinding
import com.example.skechycrag.databinding.FragmentMenuAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuAddFragment : Fragment() {


    private var _binding: FragmentMenuAddBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuAddBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addNewCragButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_menuAddFragment_to_addFragment2
            )
        }
        binding.addNewRouteButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_menuAddFragment_to_addRouteFragment
            )
        }
    }
}