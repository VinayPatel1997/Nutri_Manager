package com.example.nutri_manager.apis

import com.example.nutri_manager.models.map_models.MapResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface GoogleMapAPI {
    @GET
    suspend fun getPlaces(
        @Url()
        url: String
    ): Response<MapResponse>
}