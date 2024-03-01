package com.example.skechycrag.ui.showresponse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.skechycrag.R
import com.example.skechycrag.databinding.FragmentAddBinding
import com.example.skechycrag.databinding.FragmentShowResponseBinding

class ShowResponseFragment : Fragment() {

    private var _binding: FragmentShowResponseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowResponseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}