package com.example.deverytime_android2

import retrofit2.Response

class SearchRepository(
    private val api: SearchApi = RetrofitClient.searchApi,
    private val postApi: PostApi = RetrofitClient.postApi,
) {
    suspend fun search(
        keyword: String,
        page: Int = 0,
        size: Int = 10,
        sort: String = "createdAt,desc",
    ): Response<SearchResponse> {
        val trimmedKeyword = keyword.trim()
        require(trimmedKeyword.isNotEmpty()) { "검색어를 입력해 주세요." }
        return api.search(SearchRequest(trimmedKeyword, page, size, sort))
    }

    // 사용자별 게시글 API가 없어 전체 최신 게시글을 maxPages까지 훑으며 writerId로 작성자 글을 찾음
    suspend fun findPostsByWriters(
        writerIds: Set<Long>,
        perWriter: Int = 3,
        pageSize: Int = 50,
        maxPages: Int = 5,
    ): Map<Long, List<PostListItem>> {
        val found = writerIds.associateWith { mutableListOf<PostListItem>() }
        var page = 0
        while (page < maxPages && found.values.any { it.size < perWriter }) {
            val response = postApi.getPosts(page = page, size = pageSize, sort = "createdAt,desc")
            val result = response.body()
            val body = result?.data
            check(response.isSuccessful && result?.success == true && body != null) {
                "게시글 목록 조회 실패 (HTTP ${response.code()})"
            }
            body.content.forEach { post ->
                val posts = post.writerId?.let(found::get) ?: return@forEach
                if (posts.size < perWriter) posts += post
            }
            if (page + 1 >= body.totalPages) break
            page++
        }
        return found
    }
}
