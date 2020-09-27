package com.example.nutri_manager.models

import java.time.LocalDateTime

data class FoodConsumption(
    val date: LocalDateTime? = null,
    val foodNutrientList: Food? = null
)