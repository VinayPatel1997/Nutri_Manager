package com.example.nutri_manager.models

data class FoodResponse(
    val currentPage: Int,
    val foodSearchCriteria: FoodSearchCriteria,
    val foods: MutableList<Food>,
    val totalHits: Int,
    val totalPages: Int
)