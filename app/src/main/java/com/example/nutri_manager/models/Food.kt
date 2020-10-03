package com.example.nutri_manager.models

import java.io.Serializable

data class Food(
    val allHighlightFields: String? = null,
    val brandOwner: String? = null,
    val dataType: String? = null,
    val description: String? = null,
    val fdcId: Int? = null,
    val foodNutrients: MutableList<FoodNutrient>? = null,
    val gtinUpc: String? = null,
    val ingredients: String? = null,
    val publishedDate: String? = null,
    val score: Double? = null
) : Serializable
