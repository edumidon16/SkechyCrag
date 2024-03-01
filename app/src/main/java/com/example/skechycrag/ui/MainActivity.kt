package com.example.skechycrag.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.skechycrag.R
import com.example.skechycrag.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val onDestinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            val bottomNavView: BottomNavigationView = findViewById(R.id.bottomNavView)
            when (destination.id) {
                R.id.logInFragment -> bottomNavView.visibility = View.GONE
                R.id.addFragment2 -> bottomNavView.visibility = View.GONE
                else -> bottomNavView.visibility = View.VISIBLE
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initNavigation()
    }

    private fun initNavigation() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
        navController.addOnDestinationChangedListener(onDestinationChangedListener)
        binding.bottomNavView.setupWithNavController(navController)


        binding.bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.searchFragment -> {
                    // Navigate to the search fragment
                    navController.navigate(R.id.searchFragment)
                    true
                }
                R.id.logBookFragment -> {
                    // Navigate to the logbook fragment
                    navController.navigate(R.id.logBookFragment)
                    true
                }
                R.id.addFragment2 -> {
                    // Navigate to the add fragment
                    navController.navigate(R.id.addFragment2)
                    true
                }
                else -> false
            }
        }
    }
}