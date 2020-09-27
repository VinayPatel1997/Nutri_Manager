package com.example.nutri_manager.ui.fragments

import android.os.Build
import android.os.Bundle
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_food_view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FoodViewFragment : Fragment(R.layout.fragment_food_view) {

    lateinit var viewModel: FoodViewModel
    lateinit var nutrientAdapter: NutrientAdapter

    val args: FoodViewFragmentArgs by navArgs()
    lateinit var nutrientList: MutableList<FoodNutrient>
    lateinit var food: Food

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as FoodActivity).viewModel
        setupRecyclerView()
        food = args.food
        nutrientList = food.foodNutrients
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
                                    for (index in 0 until nutrientList.size) {
                                        nutrientList[index].value =
                                            (nutrientList[index].value * editable.toString()
                                                .toInt()) / 100
                                    }
                                }.await()
                                delay(2000)
                                hideProgressBar()
                            }
                            nutrientList.let {
                                nutrientAdapter.differ.submitList(nutrientList.toList())
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        btAdd.setOnClickListener {
            etAmount?.let {
//                val foodId = args.food.fdcId
//                val foodAmount = etAmount.text.toString().toInt()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                val curruntDateTime = LocalDateTime.now()
                val formatted = formatter.format(curruntDateTime)
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
                    food.score
                )
                uploadFoodConsumption(FoodConsumption(curruntDateTime, updatedFoodObject), formatted)
            }
        }
    }


    private fun uploadFoodConsumption(foodConsumption: FoodConsumption, date: String) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                FirebaseAuth.getInstance().uid?.let {
                    FirebaseFirestore.getInstance().collection(it).document(date)
                        .set(foodConsumption)
                }?.await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Successfully Added", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
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

//private suspend fun updateList(amount: Int) = runBlocking {
//    async {
//        for (index in 0 until nutrientList.size){
//            nutrientList[index].value = (nutrientList[index].value*amount)/100
//        }
//        Toast.makeText(context, "Start Refreshing....", Toast.LENGTH_SHORT).show()
//        nutrientList.let {
//            nutrientAdapter.differ.submitList(nutrientList.toList())
//        }
//        Toast.makeText(context, "Refreshed..", Toast.LENGTH_SHORT).show()
//    }.await()
//}