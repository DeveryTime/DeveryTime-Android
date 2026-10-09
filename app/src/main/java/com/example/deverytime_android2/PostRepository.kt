package com.example.deverytime_android2

import retrofit2.Response

// API 요청 전달 (테스트용 API 교체 가능)
class PostRepository(
    private val postApi: PostApi = RetrofitClient.postApi
) {

    // 게시글 삭제 요청
    suspend fun deletePost(id: Long): Response<Unit> {
        return postApi.deletePost(id)
    }

    // 게시글 수정 요청
    suspend fun updatePost(id: Long, request: UpdatePostRequest): Response<UpdatePostResponse> {
        return postApi.updatePost(id, request)
    }

    // 게시글 상세 조회
    suspend fun getPost(id: Long): Response<PostDetailResponse> {
        return postApi.getPost(id)
    }

    // 정렬·카테고리별 목록 조회
    suspend fun getPosts(
        page: Int = 0,
        size: Int = 20,
        sort: String = "latest",
        categoryId: Long? = null,
    ): Response<PostListResponse> {
        return postApi.getPosts(page, size, sort, categoryId)
    }

    // 카테고리 조회
    suspend fun getCategories(): Response<List<PostCategory>> {
        return postApi.getCategories()
    }

    // 게시글 등록
    suspend fun createPost(request: CreatePostRequest): Response<CreatePostResponse> {
        return postApi.createPost(request)
    }

    // 검색 요청
    suspend fun searchPosts(
        request: SearchRequest
    ): SearchResponse {
        return postApi.searchPosts(request)
    }
}
