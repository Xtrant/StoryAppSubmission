package com.example.storyappsubmission.di

import android.content.Context
import com.example.storyappsubmission.data.pref.UserPreferences
import com.example.storyappsubmission.data.UserRepository
import com.example.storyappsubmission.data.pref.dataStore
import com.example.storyappsubmission.data.retrofit.ApiConfig
import com.example.storyappsubmission.view.ViewModelFactory

object Injection {

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val repository = UserRepository.getInstance(apiService)
        return ViewModelFactory(repository, preferences)
    }
}