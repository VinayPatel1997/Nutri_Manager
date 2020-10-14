package com.example.nutri_manager.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutri_manager.repository.Repository

class ViewModelProviderFactory(
    val app: Application,
    val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModel(app, repository) as T
    }
}