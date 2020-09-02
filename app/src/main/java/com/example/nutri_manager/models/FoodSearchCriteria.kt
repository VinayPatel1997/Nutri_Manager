package com.example.nutri_manager.models

data class FoodSearchCriteria(
    val generalSearchInput: String,
    val pageNumber: Int,
    val query: String,
    val requireAllWords: Boolean
)