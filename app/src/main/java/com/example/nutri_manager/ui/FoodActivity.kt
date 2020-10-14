package com.example.nutri_manager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nutri_manager.R
import com.example.nutri_manager.repository.Repository
import kotlinx.android.synthetic.main.activity_food.*
class FoodActivity : AppCompatActivity() {
    lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)
        val foodRepository = Repository()
        val viewModelProviderFactory = ViewModelProviderFactory(application, foodRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ViewModel::class.java)
        bottomNavigationView.setupWithNavController(foodsNavHostFragment.findNavController())
    }
}