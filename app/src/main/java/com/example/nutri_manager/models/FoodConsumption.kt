package com.example.nutri_manager.models

import com.google.firebase.Timestamp

data class FoodConsumption(
    val date: HashMap<String, Timestamp>? = null,
    val foodNutrientList: Food? = null
)