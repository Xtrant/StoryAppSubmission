package com.example.storyappsubmission.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyappsubmission.result.Result
import com.example.storyappsubmission.data.pref.UserPreferences
import com.example.storyappsubmission.data.UserRepository
import com.example.storyappsubmission.data.response.UploadResponse
import kotlinx.coroutines.launch
import java.io.File

class UploadViewModel(
    private val userRepository: UserRepository,
    private val pref: UserPreferences
) : ViewModel() {

    private val _resultMessage = MutableLiveData<Result<UploadResponse>>()
    val resultMessage: LiveData<Result<UploadResponse>> = _resultMessage

    fun uploadImage(file: File, description: String, token: String) {
        viewModelScope.launch {
            _resultMessage.value = Result.Loading
            _resultMessage.value = userRepository.uploadImage(file, description, token)
        }
    }

    fun uploadImageWithLocation(
        file: File,
        description: String,
        token: String,
        lat: String,
        lon: String
    ) {
        viewModelScope.launch {
            _resultMessage.value = Result.Loading
            _resultMessage.value =
                userRepository.uploadImageWithLocation(file, description, token, lat, lon)
        }
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }
}