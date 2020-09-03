package com.example.nutrimanager.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nutri_manager.FoodsApplication
import com.example.nutri_manager.models.FoodResponse
import com.example.nutri_manager.repository.FoodRepository
import com.example.nutri_manager.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class FoodViewModel(
    app: Application,
    val foodRepository : FoodRepository
) : AndroidViewModel(app) {

    val searchFoods: MutableLiveData<Resource<FoodResponse>> = MutableLiveData()
    var searchFoodsPage = 1
    var searchFoodsResponse: FoodResponse? = null

    fun searchFoods(searchQuery: String) = viewModelScope.launch {
        safeSearchFoodsCall(searchQuery)
    }

    private fun handleSearchFoodsResponse(response: Response<FoodResponse>) : Resource<FoodResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchFoodsPage++
                if(searchFoodsResponse == null) {
                    searchFoodsResponse = resultResponse
                } else {
                    val oldFoods = searchFoodsResponse?.foods
                    val newFoods = resultResponse.foods
                    oldFoods?.addAll(newFoods)
                }
                return Resource.Success(searchFoodsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeSearchFoodsCall(searchQuery: String) {
        searchFoods.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = foodRepository.searchNews(searchQuery, searchFoodsPage)
                searchFoods.postValue(handleSearchFoodsResponse(response))
            } else {
                searchFoods.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> searchFoods.postValue(Resource.Error("Network Failure"))
                else -> searchFoods.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<FoodsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}