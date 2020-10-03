package com.example.nutri_manager.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nutri_manager.FoodsApplication
import com.example.nutri_manager.map_models.MapResponse
import com.example.nutri_manager.models.FoodConsumption
import com.example.nutri_manager.models.FoodResponse
import com.example.nutri_manager.repository.FoodRepository
import com.example.nutri_manager.util.Resource
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.Response


class FoodViewModel(
    app: Application,
    val foodRepository: FoodRepository
) : AndroidViewModel(app) {

    val searchFoods: MutableLiveData<Resource<FoodResponse>> = MutableLiveData()
    val getNearbyPlaces: MutableLiveData<Resource<MapResponse>> = MutableLiveData()
    val foodConsumption: MutableLiveData<Resource<QuerySnapshot>> = MutableLiveData()
    var searchFoodsPage = 1
    var searchFoodsResponse: FoodResponse? = null
    var getMapResponse: MapResponse? = null



    // Retrieving food from firebase

    fun getFoodConsumption() = viewModelScope.launch {
        safeGetFoodConsumptionCall()

    }
    private suspend fun safeGetFoodConsumptionCall(){
        foodConsumption.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = foodRepository.getFoodConsumption().await()
                foodConsumption.postValue(Resource.Success(response))
            } else {
                foodConsumption.postValue(Resource.Error("No internet connection"))
            }

        } catch (t: Throwable) {
            when(t) {
                is IOException -> foodConsumption.postValue(Resource.Error("Network Failure"))
                else -> foodConsumption.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    // Getting food from FoodData Central API

    fun searchFoods(searchQuery: String) = viewModelScope.launch {
        safeSearchFoodsCall(searchQuery)
    }

    private suspend fun safeSearchFoodsCall(searchQuery: String) {
        searchFoods.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.searchFoods(searchQuery, searchFoodsPage)
                searchFoods.postValue(handleSearchFoodsResponse(response))
            } else {
                searchFoods.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchFoods.postValue(Resource.Error("Network Failure"))
                else -> searchFoods.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleSearchFoodsResponse(response: Response<FoodResponse>): Resource<FoodResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchFoodsPage++
                if (searchFoodsResponse == null) {
                    searchFoodsResponse = resultResponse
                } else {
                    val oldFoods = searchFoodsResponse?.foods
                    val newFoods = resultResponse.foods
                    oldFoods?.addAll(newFoods)
                }
                return Resource.Success(searchFoodsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    // API Calls for Map

    fun getNearbyPlaces(url: String) = viewModelScope.launch {
        safeGetNearbyPlacesCall(url)
    }

    private suspend fun safeGetNearbyPlacesCall(url: String) {

        getNearbyPlaces.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.getNearbyPlaces(url)
                getNearbyPlaces.postValue(handleNearByPlaceResponse(response))
            } else {
                getNearbyPlaces.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getNearbyPlaces.postValue(Resource.Error("Network Failure"))
                else -> getNearbyPlaces.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleNearByPlaceResponse(response: Response<MapResponse>): Resource<MapResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(getMapResponse ?: resultResponse)
//                getMapResponse = resultResponse
//                searchFoodsPage++
//                if (getMapResponse == null) {
//                    getMapResponse = getMapResponse
//                } else {
////                    val oldFoods = searchFoodsResponse?.foods
////                    val newFoods = resultResponse.foods
////                    oldFoods?.addAll(newFoods)
//                }
//                Toast.makeText(getApplication(), "Success", )
            }
        }
        return Resource.Error(response.message())
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<FoodsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
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