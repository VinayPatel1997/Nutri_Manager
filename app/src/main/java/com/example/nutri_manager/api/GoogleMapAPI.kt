package com.example.nutri_manager.api

import com.example.nutri_manager.map_models.MapResponse
import com.example.nutri_manager.models.FoodResponse
import com.example.nutri_manager.util.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface GoogleMapAPI {
    @GET
    suspend fun getPlaces(
        @Url()
        url: String
    ): Response<MapResponse>
}