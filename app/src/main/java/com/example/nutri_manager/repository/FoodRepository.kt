package com.example.nutri_manager.repository

import com.example.nutri_manager.api.RetrofitInstance


class FoodRepository(
//    val db : FoodDatabase
) {

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.getFood(searchQuery, pageNumber)

//    suspend fun upsert(article: Food) = db.getFoodDao().upsert(article)

//    fun getSavedNews() = db.getArticleDao().getAllArticles()
//
//    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}