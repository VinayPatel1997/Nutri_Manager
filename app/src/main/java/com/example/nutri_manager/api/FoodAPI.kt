package com.example.nutri_manager.api

import com.example.nutri_manager.models.FoodResponse
import com.example.nutri_manager.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodAPI {
    @GET("fdc/v1/foods/search")
    suspend fun getFood(
        @Query("query")
        searchQuery: String,
        @Query("pageNumber")
        pageNumber: Int = 1,
        @Query("pageSize")
        pageSize : Int = 20,
        @Query("api_key")
        apiKey: String = API_KEY
    ): Response<FoodResponse>
}