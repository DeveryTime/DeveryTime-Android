package com.example.deverytime_android2

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Query

interface MyPageApi {
    @GET("api/users/me")
    suspend fun getMyProfile(): Response<MyProfileResponse>

    @GET("api/users/me/posts")
    suspend fun getMyPosts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "",
        @Query("categoryId") categoryId: Long? = null,
    ): Response<MyPostsResponse>

    @Multipart
    @PATCH("api/users/me")
    suspend fun updateMyProfile(
        @Part("request") request: RequestBody,
        @Part profileImage: MultipartBody.Part?,
    ): Response<UpdateProfileResponse>
}