package com.example.nutri_manager.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutri_manager.R
import com.example.nutri_manager.adapters.NutrientAdapter
import com.example.nutri_manager.ui.FoodActivity
import com.example.nutri_manager.ui.FoodViewModel
import com.example.nutri_manager.util.Constants
import kotlinx.android.synthetic.main.fragment_food_view.*
import kotlinx.android.synthetic.main.fragment_search_food.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FoodViewFragment : Fragment(R.layout.fragment_food_view) {

    lateinit var viewModel: FoodViewModel
    lateinit var nutrientAdapter: NutrientAdapter

    val args: FoodViewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as FoodActivity).viewModel
        setupRecyclerView()

        val defaultNutrientList = args.food.foodNutrients
        var nutrientList = args.food.foodNutrients

        var job: Job? = null
        etAmount.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.SEARCH_FOOD_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {

                        for (index in nutrientList.withIndex()){

                        }
                    }
                }
            }
        }

        nutrientList?.let {
            nutrientAdapter.differ.submitList(nutrientList.toList())
        }
    }
    private fun setupRecyclerView() {
        nutrientAdapter = NutrientAdapter()
        rvNutrientList.apply {
            adapter = nutrientAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}