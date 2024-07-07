package com.example.storyappsubmission.data.retrofit

import com.example.storyappsubmission.data.response.DetailStoryResponse
import com.example.storyappsubmission.data.response.ListStoryResponse
import com.example.storyappsubmission.data.response.UploadResponse
import com.example.storyappsubmission.data.response.UserLoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): UploadResponse


    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): UserLoginResponse


    @GET("stories")
    suspend fun listStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): ListStoryResponse

    @GET("stories")
    suspend fun listStoryWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1
    ): ListStoryResponse


    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): UploadResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImageWithLocation(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Header("Authorization") token: String
    ): UploadResponse


    @GET("stories/{id}")
    suspend fun detailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailStoryResponse

}

