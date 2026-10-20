package com.example.deverytime_android2

import com.google.gson.annotations.SerializedName

data class MyProfileResponse(
    val success: Boolean,
    val data: MyProfile?,
)

data class MyProfile(
    val id: Long,
    val name: String,
    val schoolNumber: String,
    val email: String,
    val username: String,
    val profileImageUrl: String?,
)

data class MyPostsResponse(
    val content: List<MyPostSummary>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
)

data class MyPostSummary(
    val id: Long,
    val title: String,
    val category: String,
    val createdAt: String,
)

data class UpdateProfileRequest(
    val username: String,
    val deleteProfileImage: Boolean,
)

data class UpdateProfileResponse(
    val success: Boolean,
    val data: Any?,
    val message: String,
)