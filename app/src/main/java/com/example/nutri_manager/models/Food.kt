package com.example.nutri_manager.models

import java.io.Serializable

data class Food(
    val allHighlightFields: String,
    val brandOwner: String,
    val dataType: String,
    val description: String,
    val fdcId: Int,
    val foodNutrients: List<FoodNutrient>,
    val gtinUpc: String,
    val ingredients: String,
    val publishedDate: String,
    val score: Double
) : Serializable