package com.example.nutri_manager.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nutri_manager.FoodsApplication
import com.example.nutri_manager.models.map_models.MapResponse
import com.example.nutri_manager.models.FoodConsumption
import com.example.nutri_manager.models.FoodResponse
import com.example.nutri_manager.models.models_progressbar.AgeWeight
import com.example.nutri_manager.models.models_progressbar.Avoid
import com.example.nutri_manager.models.models_progressbar.Take
import com.example.nutri_manager.models.UploadResponse
import com.example.nutri_manager.repository.Repository
import com.example.nutri_manager.util.Resource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okio.IOException
import retrofit2.Response


class ViewModel(
    app: Application,
    val repository: Repository
) : AndroidViewModel(app) {

    val searchFoodsMutableLiveData: MutableLiveData<Resource<FoodResponse>> = MutableLiveData()
    val getNearbyPlacesMutableLiveData: MutableLiveData<Resource<MapResponse>> = MutableLiveData()
    val foodConsumptionMutableLiveData: MutableLiveData<Resource<QuerySnapshot>> = MutableLiveData()

    val uploadFoodConsumptionMutableLiveData: MutableLiveData<Resource<UploadResponse>> = MutableLiveData()

    val uploadAgeWeightMutableLiveData: MutableLiveData<Resource<UploadResponse>> = MutableLiveData()
    val uploadAvoidNutrientMutableLiveData: MutableLiveData<Resource<UploadResponse>> = MutableLiveData()
    val uploadTakeNutrientMutableLiveData: MutableLiveData<Resource<UploadResponse>> = MutableLiveData()

    val getAgeWeightMutableLiveData: MutableLiveData<Resource<DocumentSnapshot>> = MutableLiveData()
    val getAvoidNutrientMutableLiveData: MutableLiveData<Resource<DocumentSnapshot>> = MutableLiveData()
    val getTakeNutrientMutableLiveData: MutableLiveData<Resource<DocumentSnapshot>> = MutableLiveData()


    var searchFoodsPage = 1
    var searchFoodsResponse: FoodResponse? = null
    var getMapResponse: MapResponse? = null


    // API Call Firebase (Retrieve Food Consumption)
    fun getFoodConsumption() = viewModelScope.launch {
        safeGetFoodConsumptionCall()

    }
    private suspend fun safeGetFoodConsumptionCall() {
        foodConsumptionMutableLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getFoodConsumption().await()
                foodConsumptionMutableLiveData.postValue(Resource.Success(response))
            } else {
                foodConsumptionMutableLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> foodConsumptionMutableLiveData.postValue(Resource.Error("Network Failure: ${t.message}"))
                else -> foodConsumptionMutableLiveData.postValue(Resource.Error("Conversion Error: ${t.message}"))
            }
        }
    }


    // API Call Firebase (Upload food consumption)
    fun uploadFoodConsumption(foodConsumption: FoodConsumption, docPath: String) = viewModelScope.launch {
        safeUploadFoodConsumption(foodConsumption, docPath)
    }
    private suspend fun safeUploadFoodConsumption(foodConsumption: FoodConsumption, docPath: String) {
        uploadFoodConsumptionMutableLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                repository.uploadFoodConsumption(foodConsumption, docPath).await()
                uploadFoodConsumptionMutableLiveData.postValue(Resource.SuccessFirebaseUpload("Food Consumption uploaded"))
            } else {
                uploadFoodConsumptionMutableLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> uploadFoodConsumptionMutableLiveData.postValue(Resource.Error("Network Failure: ${t.message}"))
                else -> uploadFoodConsumptionMutableLiveData.postValue(Resource.Error("Conversion Error: ${t.message}"))
            }
        }
    }

//*************************************** Preferences Start **************************************************
    // API Call Firebase (Get Preferences Age Weight)
    fun getAgeWeight() = viewModelScope.launch {
        safeGetAgeWeight()
    }
    private suspend fun safeGetAgeWeight() {
        getAgeWeightMutableLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getAgeWeight().await()
                getAgeWeightMutableLiveData.postValue(Resource.Success(response))
            } else {
                getAgeWeightMutableLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getAgeWeightMutableLiveData.postValue(Resource.Error("Network Failure: ${t.message}"))
                else -> getAgeWeightMutableLiveData.postValue(Resource.Error("Conversion Error: ${t.message}"))
            }
        }
    }


    // API Call Firebase (Get Preferences Avoid)
    fun getAvoidNutrient() = viewModelScope.launch {
        safeGetAvoidNutrient()
    }
    private suspend fun safeGetAvoidNutrient() {
        getAvoidNutrientMutableLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getAvoidNutrient().await()
                getAvoidNutrientMutableLiveData.postValue(Resource.Success(response))
            } else {
                getAvoidNutrientMutableLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getAvoidNutrientMutableLiveData.postValue(Resource.Error("Network Failure: ${t.message}"))
                else -> getAvoidNutrientMutableLiveData.postValue(Resource.Error("Conversion Error: ${t.message}"))
            }
        }
    }


    // API Call Firebase (Get Preferences Age Weight)
    fun getTakeNutrient() = viewModelScope.launch {
        safeGetTakeNutrient()
    }
    private suspend fun safeGetTakeNutrient() {
        getTakeNutrientMutableLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getTakeNutrient().await()
                getTakeNutrientMutableLiveData.postValue(Resource.Success(response))
            } else {
                getTakeNutrientMutableLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getTakeNutrientMutableLiveData.postValue(Resource.Error("Network Failure: ${t.message}"))
                else -> getTakeNutrientMutableLiveData.postValue(Resource.Error("Conversion Error: ${t.message}"))
            }
        }
    }


    // API Call Firebase (Upload Preferences Age Weight)
    fun uploadAgeWeight(ageWeightPara: AgeWeight) = viewModelScope.launch {
        safeUploadAgeWeight(ageWeightPara)
    }
    private suspend fun safeUploadAgeWeight(ageWeightPara: AgeWeight) {
        uploadAgeWeightMutableLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                repository.uploadAgeWeight(ageWeightPara).await()
                uploadAgeWeightMutableLiveData.postValue(Resource.SuccessFirebaseUpload("Age and weight uploaded successfully"))
            } else {
                foodConsumptionMutableLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> uploadAgeWeightMutableLiveData.postValue(Resource.Error("Network Failure: ${t.message}"))
                else -> uploadAgeWeightMutableLiveData.postValue(Resource.Error("Conversion Error: ${t.message}"))
            }
        }
    }


    // API Call Firebase (Upload Preferences Avoid nutrient)
    fun uploadAvoidNutrient(avoidNutrientPara: Avoid) = viewModelScope.launch {
        safeUploadAvoidNutrient(avoidNutrientPara)
    }
    private suspend fun safeUploadAvoidNutrient(avoidNutrientPara: Avoid) {
        uploadAvoidNutrientMutableLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                repository.uploadAvoid(avoidNutrientPara).await()
                uploadAvoidNutrientMutableLiveData.postValue(Resource.SuccessFirebaseUpload("Nutrient to avoid uploaded successfully"))
            } else {
                foodConsumptionMutableLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> uploadAvoidNutrientMutableLiveData.postValue(Resource.Error("Network Failure: ${t.message}"))
                else -> uploadAvoidNutrientMutableLiveData.postValue(Resource.Error("Conversion Error: ${t.message}"))
            }
        }
    }


    // API Call Firebase (Upload Preferences Take Nutrient)
    fun uploadTakeNutrient(takeNutrientPara: Take) = viewModelScope.launch {
        safeUploadTakeNutrient(takeNutrientPara)
    }
    private suspend fun safeUploadTakeNutrient(takeNutrientPara: Take) {
        uploadTakeNutrientMutableLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                repository.uploadtake(takeNutrientPara).await()
                uploadTakeNutrientMutableLiveData.postValue(Resource.SuccessFirebaseUpload("Nutrient to take uploaded successfully"))
            } else {
                foodConsumptionMutableLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> uploadTakeNutrientMutableLiveData.postValue(Resource.Error("Network Failure"))
                else -> uploadTakeNutrientMutableLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
// ***************************************************** Preferences End ************************************************


// ***************************************************** Food Data Central API Call **************************************
    // API Call FoodData Central (Search Food)
    fun searchFoods(searchQuery: String) = viewModelScope.launch {
        safeSearchFoodsCall(searchQuery)
    }
    private suspend fun safeSearchFoodsCall(searchQuery: String) {
        searchFoodsMutableLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.searchFoods(searchQuery, searchFoodsPage)
                searchFoodsMutableLiveData.postValue(handleSearchFoodsResponse(response))
            } else {
                searchFoodsMutableLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchFoodsMutableLiveData.postValue(Resource.Error("Network Failure"))
                else -> searchFoodsMutableLiveData.postValue(Resource.Error("Conversion Error"))
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

// ***************************************** Google Map ************************************************
    // API Calls for Map

    fun getNearbyPlaces(url: String) = viewModelScope.launch {
        safeGetNearbyPlacesCall(url)
    }

    private suspend fun safeGetNearbyPlacesCall(url: String) {
        getNearbyPlacesMutableLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getNearbyPlaces(url)
                getNearbyPlacesMutableLiveData.postValue(handleNearByPlaceResponse(response))
            } else {
                getNearbyPlacesMutableLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getNearbyPlacesMutableLiveData.postValue(Resource.Error("Network Failure"))
                else -> getNearbyPlacesMutableLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private fun handleNearByPlaceResponse(response: Response<MapResponse>): Resource<MapResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(getMapResponse ?: resultResponse)
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