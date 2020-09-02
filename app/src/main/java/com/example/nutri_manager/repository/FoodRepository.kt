package com.example.nutri_manager.repository

import com.example.nutri_manager.api.RetrofitInstance
//import com.example.nutrimanager.db.FoodDatabase
//import com.example.nutrimanager.models.Food

class FoodRepository(
//    val db : FoodDatabase
) {

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.getFoodNutrition(searchQuery, pageNumber)

//    suspend fun upsert(article: Food) = db.getFoodDao().upsert(article)

//    fun getSavedNews() = db.getArticleDao().getAllArticles()
//
//    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}