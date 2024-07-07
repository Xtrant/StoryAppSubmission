package com.example.storyappsubmission.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyappsubmission.data.response.DetailStoryResponse
import com.example.storyappsubmission.data.response.ErrorResponse
import com.example.storyappsubmission.data.response.ListStoryItem
import com.example.storyappsubmission.data.response.ListStoryResponse
import com.example.storyappsubmission.data.response.UploadResponse
import com.example.storyappsubmission.data.response.UserLoginResponse
import com.example.storyappsubmission.data.retrofit.ApiService
import com.example.storyappsubmission.result.Result
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository(private val apiService: ApiService) {

    suspend fun login(email: String, password: String): Result<UserLoginResponse> {
        return try {
            val response = apiService.login(email, password)
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Result.Error(errorMessage ?: "Unknown Error in catch Http Exception")
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<UploadResponse> {
        return try {
            val response = apiService.register(name, email, password)
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Result.Error(errorMessage ?: "Unknown Error in catch Http Exception")
        }
    }

    suspend fun uploadImage(
        file: File,
        description: String,
        token: String,
    ): Result<UploadResponse> {
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        return try {
            val response = apiService.uploadImage(multipartBody, requestBody, "Bearer $token")
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Result.Error(errorMessage ?: "Unknown Error in catch Http Exception")

        }
    }

    suspend fun uploadImageWithLocation(
        file: File,
        description: String,
        token: String,
        latUpload: String,
        lonUpload: String
    ): Result<UploadResponse> {
        val requestBodyDescription = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val requestBodyLan = latUpload.toRequestBody("text/plain".toMediaType())
        val requestBodyLon = lonUpload.toRequestBody("text/plain".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        return try {
            val response = apiService.uploadImageWithLocation(multipartBody, requestBodyDescription, requestBodyLan, requestBodyLon, "Bearer $token")
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Result.Error(errorMessage ?: "Unknown Error in catch Http Exception")

        }
    }

    fun listStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                pagingSourceFactory = {
                    StoryPagingSource(apiService, "bearer $token")
                }
            ).liveData

    }

    suspend fun listStoryWithLocation(token: String): Result<ListStoryResponse> {
        return try {
            val response = apiService.listStoryWithLocation("bearer $token")
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Result.Error(errorMessage ?: "Unknown Error in catch Http Exception")
        }
    }

    suspend fun detailStory(id: String, token: String): Result<DetailStoryResponse> {
        return try {
            val response = apiService.detailStory("Bearer $token", id)
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Result.Error(errorMessage ?: "Unknown Error in catch Http Exception")
        }
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}

