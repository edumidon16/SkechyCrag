package com.example.skechycrag.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.skechycrag.R
import com.example.skechycrag.databinding.FragmentLogInBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : Fragment() {


    //Binding
    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLogInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
    }

    private fun initUI() {
        initUIState()
    }

    private fun initUIState() {
        binding.btnLogin.setOnClickListener{
            var emailAddress = binding.etEmailAdrress.text
            var password = binding.etPassword.text

            if(emailAddress.toString() != "" && password.toString() != "" ){
                //Check with the database
                userViewModel.checkUser(emailAddress.toString(), password.toString())
            }
            else{
                Snackbar.make(requireView(), "Error: Enter all the information", Snackbar.LENGTH_SHORT).show()
            }
        }
        //Nos conectamos al state del VIEWMODEL, de esta forma cuando el state del viewmodel cambie, el fragment cambia.
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.userState.collect {
                    when (it) {
                        is UserDetailState.Error -> errorState()
                        UserDetailState.Loading -> loadingState()
                        is UserDetailState.Success -> successState()
                    }
                }
            }
        }
    }

    private fun successState() {
        navigateToMenu()
    }

    private fun loadingState() {

    }

    private fun errorState() {
        Snackbar.make(requireView(), "Error: Wrong user", Snackbar.LENGTH_SHORT).show()
    }
    private fun navigateToMenu() {
        findNavController().navigate(
            R.id.action_logInFragment_to_searchFragment
        )
    }

}