package com.example.nutri_manager.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutri_manager.R
import com.example.nutri_manager.adapters.NutrientAdapter
import com.example.nutri_manager.ui.FoodActivity
import com.example.nutri_manager.ui.FoodViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_food_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.HashMap


class FoodViewFragment : Fragment(R.layout.fragment_food_view) {

    lateinit var viewModel: FoodViewModel
    lateinit var nutrientAdapter: NutrientAdapter

    val args: FoodViewFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as FoodActivity).viewModel
        setupRecyclerView()


        var nutrientList = args.food.foodNutrients
        nutrientList?.let {
            nutrientAdapter.differ.submitList(nutrientList.toList())
        }
        btAdd.setOnClickListener {
            etAmount?.let {
                val foodId = args.food.fdcId
                val foodAmount = etAmount.text.toString().toInt()
                val localDate = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                val formatted = formatter.format(localDate)

                Toast.makeText(context, formatted.toString(), Toast.LENGTH_SHORT).show()
                val map = HashMap<String, Any>()
                map.put("foodId", foodId)
                map.put("foodAmount", foodAmount)
                saveFoodConsumption(map, formatted)
            }
        }
    }

    private fun saveFoodConsumption(foodConsumption: HashMap<String, Any>, date: String) =
        CoroutineScope(Dispatchers.IO).launch {
            try {

                var userID = FirebaseAuth.getInstance().uid
                val userDocumentRef =
                    userID?.let {
                        FirebaseFirestore.getInstance().collection(it).document(date)
                            .set(foodConsumption)
                    }?.await()
                Toast.makeText(context, "Successfully Added!", Toast.LENGTH_SHORT).show()
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
}