package com.example.nutri_manager.models

data class FoodNutrient(
    val derivationCode: String,
    val derivationDescription: String,
    val nutrientId: Int,
    val nutrientName: String,
    val nutrientNumber: String,
    val unitName: String,
    var value: Double
)