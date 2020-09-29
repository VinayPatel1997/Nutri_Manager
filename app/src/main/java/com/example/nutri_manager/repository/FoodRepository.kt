package com.example.nutri_manager.repository

import com.example.nutri_manager.api.RetrofitInstance


class FoodRepository(
//    val db : FoodDatabase-
) {

    suspend fun searchFoods(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.getFood(searchQuery, pageNumber)

    suspend fun getNearbyPlaces(url: String) =
        RetrofitInstance.mapAPI.getPlaces(url)


    //Firebase
    suspend fun insertFoodConsumptionToFirebase() {

    }

//    suspend fun upsert(article: Food) = db.getFoodDao().upsert(article)

//    fun getSavedNews() = db.getArticleDao().getAllArticles()
//
//    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}