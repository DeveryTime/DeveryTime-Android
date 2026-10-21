package com.example.deverytime_android2

// success/data 래퍼 없이 반환되는 서버의 상세 조회 응답
data class PostDetailApiResponse(
    val id: Long,
    val title: String?,
    val content: String?,
    val status: String?,
    val viewCount: Int,
    val writer: PostWriter?,
    val category: PostDetailCategory?,
    val images: List<PostImage>?,
    val createdAt: String?,
    val updatedAt: String?,
    val likeCount: Int?,
    val liked: Boolean?,
) {
    // 필수 값 확인 후 작성자·카테고리·이미지 ID와 정렬 순서를 그대로 전달
    internal fun toPostDetail(): PostDetailResponse = PostDetailResponse(
        id = id,
        title = requireNotNull(title) { "상세 응답의 title이 없습니다." },
        content = requireNotNull(content) { "상세 응답의 content가 없습니다." },
        status = requireNotNull(status) { "상세 응답의 status가 없습니다." },
        viewCount = viewCount,
        writer = requireNotNull(writer) { "상세 응답의 writer가 없습니다." }.also {
            requireNotNull(it.nickname) { "상세 응답의 writer.nickname이 없습니다." }
        },
        category = requireNotNull(category) { "상세 응답의 category가 없습니다." }.also {
            requireNotNull(it.name) { "상세 응답의 category.name이 없습니다." }
        },
        images = images.orEmpty().onEach {
            requireNotNull(it.imageUrl) { "상세 응답의 images.imageUrl이 없습니다." }
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
