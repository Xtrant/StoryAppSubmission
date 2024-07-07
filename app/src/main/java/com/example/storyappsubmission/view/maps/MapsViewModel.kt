package com.example.storyappsubmission.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyappsubmission.result.Result
import com.example.storyappsubmission.data.pref.UserPreferences
import com.example.storyappsubmission.data.UserRepository
import com.example.storyappsubmission.data.response.ListStoryResponse
import kotlinx.coroutines.launch

class MapsViewModel(private val userRepository: UserRepository,private val pref: UserPreferences) : ViewModel() {

    private val _resultLocation = MutableLiveData<Result<ListStoryResponse>>()
    val resultLocation: LiveData<Result<ListStoryResponse>> = _resultLocation

    fun getStoryLocation(token: String) {
        viewModelScope.launch {
            _resultLocation.value = Result.Loading
            _resultLocation.value = userRepository.listStoryWithLocation(token)
        }
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

}