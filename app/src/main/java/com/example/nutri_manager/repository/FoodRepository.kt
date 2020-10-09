package com.example.nutri_manager.repository

import com.example.nutri_manager.api.RetrofitInstance
import com.example.nutri_manager.models.models_progressbar.AgeWeight
import com.example.nutri_manager.models.models_progressbar.Avoid
import com.example.nutri_manager.models.models_progressbar.Take
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FoodRepository(
//    val db : FoodDatabase-
) {


    val userId = FirebaseAuth.getInstance().uid

    val collectionRefFoodConsumption = userId!!.let {
        FirebaseFirestore.getInstance().collection(it)
    }

    val collectionRefPreferences = userId!!.let {
        FirebaseFirestore.getInstance().collection("preferences")
    }

    suspend fun searchFoods(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.getFood(searchQuery, pageNumber)

    suspend fun getNearbyPlaces(url: String) =
        RetrofitInstance.mapAPI.getPlaces(url)

    suspend fun getFoodConsumption() =
        collectionRefFoodConsumption.get()


    suspend fun getTodaysFoodConsumption() = collectionRefFoodConsumption.document()


    // uploading preferences
    suspend fun uploadAgeWeight(ageWeight: AgeWeight) =
        collectionRefPreferences.document("ageWeight$userId").set(ageWeight)

    suspend fun uploadAvoid(avoid: Avoid) =
        collectionRefPreferences.document("avoid$userId").set(avoid)

    suspend fun uploadtake(take: Take) =
        collectionRefPreferences.document("take$userId").set(take)


//    // get preferences
    suspend fun getAgeWeight() =
        collectionRefPreferences.document("ageWeight$userId").get()
    suspend fun getAvoidNutrient() =
        collectionRefPreferences.document("avoid$userId").get()
    suspend fun getTakeNutrient() =
        collectionRefPreferences.document("take$userId").get()

}