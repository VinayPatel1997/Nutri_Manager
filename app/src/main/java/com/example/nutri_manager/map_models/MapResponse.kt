package com.example.nutri_manager.map_models

data class MapResponse(
    val html_attributions: MutableList<Any>,
    val results: MutableList<Result>,
    val status: String
)