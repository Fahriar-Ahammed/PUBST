package com.app.pub_st

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class BottomNavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)

        val bottomNavigationView = findViewById<BottomNavigationView
                >(R.id.bottom_navigation)
        val navController = findNavController(R.id.bottom_nav_fragment)
        bottomNavigationView.setupWithNavController(navController)

        NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.dashboard -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.classroom -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.notice -> {
                    // Respond to navigation item 2 click
                    true
                }
                else -> false
            }
        }
    }
}