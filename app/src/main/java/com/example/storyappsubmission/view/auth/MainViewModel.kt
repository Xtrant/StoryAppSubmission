package com.example.storyappsubmission.view.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyappsubmission.result.Result
import com.example.storyappsubmission.data.pref.UserPreferences
import com.example.storyappsubmission.data.UserRepository
import com.example.storyappsubmission.data.response.DetailStoryResponse
import com.example.storyappsubmission.data.response.ListStoryItem
import com.example.storyappsubmission.data.response.ListStoryResponse
import com.example.storyappsubmission.data.response.UploadResponse
import com.example.storyappsubmission.data.response.UserLoginResponse
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository, private val pref: UserPreferences) :
    ViewModel() {

    private val _resultLogin = MutableLiveData<Result<UserLoginResponse>>()
    val resultLogin: LiveData<Result<UserLoginResponse>> = _resultLogin

    private val _resultMessage = MutableLiveData<Result<UploadResponse>>()
    val resultMessage: LiveData<Result<UploadResponse>> = _resultMessage

    private val _resultDetailStory = MutableLiveData<Result<DetailStoryResponse>>()
    val resultDetailStory: LiveData<Result<DetailStoryResponse>> = _resultDetailStory


    fun login(email: String, password: String) {
        viewModelScope.launch {
            _resultLogin.value = Result.Loading
            _resultLogin.value = userRepository.login(email, password)
        }
    }

    fun register(email: String, name: String, password: String) {
        viewModelScope.launch {
            _resultMessage.value = Result.Loading
            _resultMessage.value = userRepository.register(name, email, password)
        }
    }

    fun story(token: String): LiveData<PagingData<ListStoryItem>> {
        return userRepository.listStory(token).cachedIn(viewModelScope)
    }


    fun getDetailStory(id: String, token: String) {
        viewModelScope.launch {
            _resultDetailStory.value = Result.Loading
            _resultDetailStory.value = userRepository.detailStory(id, token)
        }
    }

    fun isLogin(): LiveData<Boolean> {
        return pref.isLogin().asLiveData()
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveSession(token: String, isLogin: Boolean) {
        viewModelScope.launch {
            pref.saveSession(token, isLogin)
        }
    }

    fun clearSession() {
        viewModelScope.launch {
            pref.clearSession()
        }
    }
}

