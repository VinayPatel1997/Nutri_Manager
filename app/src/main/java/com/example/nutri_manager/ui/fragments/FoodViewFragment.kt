package com.example.nutri_manager.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutri_manager.R
import com.example.nutri_manager.adapters.NutrientAdapter
import com.example.nutri_manager.models.Food
import com.example.nutri_manager.models.FoodConsumption
import com.example.nutri_manager.models.FoodNutrient
import com.example.nutri_manager.ui.FoodActivity
import com.example.nutri_manager.ui.FoodViewModel
import com.example.nutri_manager.util.Constants
import com.example.nutri_manager.util.Resource
import com.google.common.collect.ImmutableList
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.fragment_food_view.*
import kotlinx.coroutines.*
import okhttp3.internal.toImmutableList


class FoodViewFragment : Fragment(R.layout.fragment_food_view) {

    lateinit var viewModel: FoodViewModel
    lateinit var nutrientAdapter: NutrientAdapter

    val args: FoodViewFragmentArgs by navArgs()
    lateinit var food: Food
    final lateinit var defaultNutrientList: ImmutableList<FoodNutrient>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as FoodActivity).viewModel
        setupRecyclerView()
        food = args.food
        defaultNutrientList = ImmutableList.copyOf(food.foodNutrients)
        val nutrientList: MutableList<FoodNutrient> = arrayListOf()
        nutrientList.addAll(defaultNutrientList)

//        val defaultArray: Array<FoodNutrient> = defaultNutrientList.toTypedArray();
////        for (item in defaultArray){
////            val iteem : FoodNutrient = item;
////            nutrientList.add(iteem);
////        }
        nutrientAdapter.differ.submitList(nutrientList.toList())

        var job: Job? = null
        etAmount.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.SEARCH_FOOD_TIME_DELAY)
                editable?.let {
                    if (editable.toString().trim().isNotEmpty()) {
                        try {
                            showProgressBar()
                            coroutineScope {
                                async {
                                    for (index in 0 until defaultNutrientList.size) {
                                        val defValue = defaultNutrientList[index].value
                                        Log.d("AAAAAAAA", "def before:$defValue")
                                        nutrientList[index].value = (defValue?.times(editable.toString().toInt()))?.div(100)
                                        val defAfter = defaultNutrientList[index].value
                                        Log.d("AAAAAAAA", "def after:$defAfter")
                                    }
                                }.await()
                                delay(2000)
                                hideProgressBar()
                            }
                            nutrientList.let {
                                nutrientAdapter.differ.submitList(nutrientList.toList())
                                nutrientAdapter.notifyDataSetChanged()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        btAdd.setOnClickListener {
            val timestamp = Timestamp.now()
            val updatedFoodObject = Food(
                food.allHighlightFields,
                food.brandOwner,
                food.dataType,
                food.description,
                food.fdcId,
                nutrientList,
                food.gtinUpc,
                food.ingredients,
                food.publishedDate,
                food.score,
            )

            val currentDateTime: HashMap<String, Timestamp> = HashMap()
            currentDateTime.put("date", timestamp)

            viewModel.uploadFoodConsumption(
                FoodConsumption(currentDateTime, updatedFoodObject),
                timestamp.toString()
            )
            viewModel.uploadFoodConsumptionMutableLiveData.observe(viewLifecycleOwner, { response ->
                when (response) {
                    is Resource.SuccessFirebaseUpload -> {
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                            .show()
                        hideProgressBar()
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                            .show()
                        hideProgressBar()
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })
        }
    }


    private fun setupRecyclerView() {
        nutrientAdapter = NutrientAdapter()
        rvNutrientList.apply {
            adapter = nutrientAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar() {
        paginationProgressBarFoodViewFrag.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBarFoodViewFrag.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
}
