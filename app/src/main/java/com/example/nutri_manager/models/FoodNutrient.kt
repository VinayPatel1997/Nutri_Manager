package com.example.nutri_manager.models

data class FoodNutrient(
    val derivationCode: String? = null,
    val derivationDescription: String? = null,
    val nutrientId: Int? = null,
    val nutrientName: String? = null,
    val nutrientNumber: String? = null,
    val unitName: String? = null,
    var value: Double? = null
)