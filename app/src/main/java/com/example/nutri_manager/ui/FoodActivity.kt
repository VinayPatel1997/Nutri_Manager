package com.example.nutri_manager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nutri_manager.R
import kotlinx.android.synthetic.main.activity_food.*

class FoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        bottomNavigationView.setupWithNavController(foodsNavHostFragment.findNavController())
    }
}