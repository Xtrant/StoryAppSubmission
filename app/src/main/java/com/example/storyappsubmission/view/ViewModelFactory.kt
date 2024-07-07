package com.example.storyappsubmission.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.data.pref.UserPreferences
import com.example.storyappsubmission.data.UserRepository
import com.example.storyappsubmission.di.Injection
import com.example.storyappsubmission.view.upload.UploadViewModel
import com.example.storyappsubmission.view.auth.MainViewModel
import com.example.storyappsubmission.view.maps.MapsViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userRepository, userPreferences) as T
        }

        if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            return UploadViewModel(userRepository, userPreferences) as T
        }

        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(userRepository, userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)

    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                Injection.provideViewModelFactory(context)
            }.also { INSTANCE = it }
    }
}