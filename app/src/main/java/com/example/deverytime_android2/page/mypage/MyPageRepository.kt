package com.example.deverytime_android2

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class MyPageRepository(
    private val api: MyPageApi = RetrofitClient.myPageApi,
    private val usernameApi: UsernameApi = RetrofitClient.usernameApi,
    private val profileImageReader: ProfileImageReader,
) {
    private val gson = Gson()

    suspend fun getMyProfile(): Response<MyProfileResponse> {
        return api.getMyProfile()
    }

    suspend fun getMyPosts(
        page: Int = 0,
        size: Int = 20,
        sort: String? = null,
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
        username: String,
        deleteProfileImage: Boolean,
        profileImageUri: String?,
    ): Response<UpdateProfileResponse> {
        val request =
            UpdateProfileRequest(
                username = username,
                deleteProfileImage = deleteProfileImage,
            )
        val requestBody =
            gson
                .toJson(request)
                .toRequestBody("application/json".toMediaTypeOrNull())

        return api.updateMyProfile(
            request = requestBody,
            profileImage =
                createProfileImagePart(
                    uriString = profileImageUri,
                ),
        )
    }

    private suspend fun createProfileImagePart(
        uriString: String?,
    ): MultipartBody.Part? {
        if (uriString == null) {
            return null
        }

        val imageContent =
            profileImageReader.read(uriString)
                ?: return null

        val imageBody =
            imageContent.bytes.toRequestBody(
                imageContent.mediaType?.toMediaTypeOrNull(),
            )

        return MultipartBody.Part.createFormData(
            name = "profileImage",
            filename = "profile.jpg",
            body = imageBody,
        )
    }

    suspend fun checkUsername(
        username: String,
    ): Response<CheckUsernameResponse> {
        return usernameApi.checkUsername(username)
    }
}
