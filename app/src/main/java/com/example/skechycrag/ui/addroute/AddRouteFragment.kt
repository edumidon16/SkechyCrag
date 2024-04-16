package com.example.skechycrag.ui.addroute

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.skechycrag.R
import com.example.skechycrag.databinding.FragmentAddRouteBinding
import com.example.skechycrag.databinding.FragmentMenuAddBinding
import com.example.skechycrag.ui.add.AddViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRouteFragment : Fragment() {

    private var _binding: FragmentAddRouteBinding? = null
    private val binding get() = _binding!!

    private val addRouteViewModel: AddRouteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddRouteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_addRouteFragment_to_menuAddFragment
            )
        }
        binding.addButton.setOnClickListener {
            addRouteViewModel.addRoute(binding.cragNameEditText.text.toString(), binding.routeNameEditText.text.toString(), binding.routeGradeEditText.text.toString(), binding.climbingTypeEditText.text.toString())
            Toast.makeText(context, "Route has been saved", Toast.LENGTH_SHORT).show()
            findNavController().navigate(
                R.id.action_addRouteFragment_to_menuAddFragment
            )
        }
    }

}