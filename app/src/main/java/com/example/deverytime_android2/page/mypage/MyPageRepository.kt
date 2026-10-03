package com.example.deverytime_android2

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class MyPageRepository(
    private val api: MyPageApi = RetrofitClient.myPageApi,
    private val signUpApi: SignUpApi = RetrofitClient.signUpApi,
) {
    suspend fun getMyProfile(): Response<MyProfileResponse> {
        return api.getMyProfile()
    }

    suspend fun getMyPosts(
        page: Int = 0,
        size: Int = 20,
        sort: String = "",
        categoryId: Long? = null,
    ): Response<MyPostsResponse> {
        return api.getMyPosts(
            page = page,
            size = size,
            sort = sort,
            categoryId = categoryId,
        )
    }

    suspend fun updateMyProfile(
        request: RequestBody,
        profileImage: MultipartBody.Part?,
    ): Response<UpdateProfileResponse> {
        return api.updateMyProfile(
            request = request,
            profileImage = profileImage,
        )
    }

    suspend fun checkUsername(
        username: String,
    ): Response<CheckUsernameResponse> {
        return signUpApi.checkUsername(username)
    }
}