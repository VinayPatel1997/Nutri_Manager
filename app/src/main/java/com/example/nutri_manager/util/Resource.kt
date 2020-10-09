package com.example.nutri_manager.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class SuccessFirebaseUpload<T>(message: String, data: T? = null): Resource<T>(data,message)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}