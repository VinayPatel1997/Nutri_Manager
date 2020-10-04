package com.example.nutri_manager.models

import com.google.firebase.Timestamp
import java.time.LocalDateTime

data class FoodConsumption(
    val date: HashMap<String, Timestamp>? = null,
    val foodNutrientList: Food? = null
)