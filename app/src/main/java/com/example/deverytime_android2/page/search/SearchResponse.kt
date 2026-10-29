package com.example.deverytime_android2

data class SearchResponse(
    val success: Boolean,
    val data: SearchData?,
    val error: ApiError? = null,
)

data class SearchData(
    val users: List<SearchUserResult>?,
    val posts: PostListPage?,
)

data class SearchUserResult(
    val id: Long,
    val username: String?,
    val nickname: String?,
    val profileImageUrl: String? = null,
) {
    val displayName: String
        get() = nickname?.takeIf { it.isNotBlank() }
            ?: username?.takeIf { it.isNotBlank() }
            ?: "사용자"
}
