package com.example.deverytime_android2

// 서버의 상세 조회 응답
data class PostDetailApiResponse(
    val success: Boolean,
    val data: PostDetailData?,
)

data class PostDetailData(
    val id: Long,
    val title: String?,
    val content: String?,
    val status: String?,
    val viewCount: Int,
    val writerNickname: String?,
    val writerProfileImageUrl: String?,
    val categoryName: String?,
    val imageUrls: List<String>?,
    val createdAt: String?,
    val updatedAt: String?,
    val likeCount: Int?,
    val liked: Boolean?,
) {
    // 필수 값은 화면에 전달하기 전에 확인하고, 응답에 없는 ID는 null로 유지
    internal fun toPostDetail(): PostDetailResponse = PostDetailResponse(
        id = id,
        title = requireNotNull(title) { "상세 응답의 title이 없습니다." },
        content = requireNotNull(content) { "상세 응답의 content가 없습니다." },
        status = requireNotNull(status) { "상세 응답의 status가 없습니다." },
        viewCount = viewCount,
        writer = PostWriter(
            userId = null,
            nickname = requireNotNull(writerNickname) { "상세 응답의 writerNickname이 없습니다." },
            profileImageUrl = writerProfileImageUrl,
        ),
        category = PostDetailCategory(
            id = null,
            name = requireNotNull(categoryName) { "상세 응답의 categoryName이 없습니다." },
        ),
        images = imageUrls.orEmpty().mapIndexed { index, url ->
            PostImage(id = null, imageUrl = url, sortOrder = index)
        },
        createdAt = requireNotNull(createdAt) { "상세 응답의 createdAt이 없습니다." },
        updatedAt = updatedAt,
        likeCount = likeCount,
        liked = liked,
    )
}

// 기존 화면에서 사용하는 게시글 상세 정보
data class PostDetailResponse(
    val id: Long,
    val title: String,
    val content: String,
    val status: String,
    val viewCount: Int,
    val writer: PostWriter,
    val category: PostDetailCategory,
    val images: List<PostImage>,
    val createdAt: String,
    val updatedAt: String?,
    val likeCount: Int? = null,
    val liked: Boolean? = null,
)

// 작성자 정보
data class PostWriter(
    val userId: Long?,
    val nickname: String,
    val profileImageUrl: String?,
)

// 상세 카테고리 정보
data class PostDetailCategory(
    val id: Long?,
    val name: String,
)

// 첨부 이미지·표시 순서
data class PostImage(
    val id: Long?,
    val imageUrl: String,
    val sortOrder: Int,
)
