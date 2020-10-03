package com.example.nutri_manager.repository

import android.widget.Toast
import com.example.nutri_manager.api.RetrofitInstance
import com.example.nutri_manager.models.FoodConsumption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class FoodRepository(
//    val db : FoodDatabase-
) {


    val collectionRef = FirebaseAuth.getInstance().uid!!.let {
        FirebaseFirestore.getInstance().collection(
            it
        )
    }
    suspend fun searchFoods(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.getFood(searchQuery, pageNumber)

    suspend fun getNearbyPlaces(url: String) =
        RetrofitInstance.mapAPI.getPlaces(url)

    suspend fun getFoodConsumption() =
        collectionRef.get()
}