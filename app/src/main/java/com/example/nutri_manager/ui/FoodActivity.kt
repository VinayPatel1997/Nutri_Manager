package com.example.nutri_manager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nutri_manager.R
import com.example.nutri_manager.repository.FoodRepository
import kotlinx.android.synthetic.main.activity_food.*
class FoodActivity : AppCompatActivity() {
    lateinit var viewModel: FoodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)
//        currentUID = intent.getStringExtra("userID")
        val foodRepository = FoodRepository()
        val viewModelProviderFactory = FoodViewModelProviderFactory(application, foodRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(FoodViewModel::class.java)
        bottomNavigationView.setupWithNavController(foodsNavHostFragment.findNavController())
    }
}