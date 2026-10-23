package com.example.deverytime_android2

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.google.gson.JsonParser
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostMarkdownTest {
    private val text = "굵게 기울임 밑줄 취소선 링크"
    private val link = "https://example.test/page?q=1&view=2"

    private fun range(word: String): TextRange {
        val start = text.indexOf(word)
        return TextRange(start, start + word.length)
    }

    private fun formattedEditor() = RichTextState().apply {
        setText(text)
        addSpanStyle(SpanStyle(fontWeight = FontWeight.Bold), range("굵게"))
        addSpanStyle(SpanStyle(fontStyle = FontStyle.Italic), range("기울임"))
        addSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline), range("밑줄"))
        addSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough), range("취소선"))
        selection = range("링크")
        addLinkToSelection(link)
    }

    @Test
    fun exportingEditorIncludesFormattingAndLinkDestination() {
        val markdown = formattedEditor().toPostMarkdown()
        assertTrue(markdown.contains("**굵게**"))
        assertTrue(markdown.contains("*기울임*"))
        assertTrue(markdown.contains("<u>밑줄</u>"))
        assertTrue(markdown.contains("~~취소선~~"))
        assertTrue(markdown.contains("[링크]($link)"))
    }

    @Test
    fun loadingSavedMarkdownRestoresEditableStyles() {
        val saved = formattedEditor().toPostMarkdown()
        val restored = RichTextState().setMarkdown(saved)
        assertEquals(text, restored.annotatedString.text)
        assertEquals(FontWeight.Bold, restored.getSpanStyle(range("굵게")).fontWeight)
        assertEquals(FontStyle.Italic, restored.getSpanStyle(range("기울임")).fontStyle)
        assertTrue(restored.getSpanStyle(range("밑줄")).textDecoration!!.contains(TextDecoration.Underline))
        assertTrue(restored.getSpanStyle(range("취소선")).textDecoration!!.contains(TextDecoration.LineThrough))
        assertTrue(restored.toPostMarkdown().contains("[링크]($link)"))
    }

    @Test
    fun plainTextStillLoadsAndBlankContentRemainsBlank() {
        val plain = "기존 일반 텍스트 게시글"
        assertEquals(plain, RichTextState().setMarkdown(plain).annotatedString.text)
        assertEquals("", RichTextState().toPostMarkdown())
        assertEquals("", RichTextState().setText("   \n\t").toPostMarkdown())
    }

    @Test
    fun createAndUpdateRequestsSendMarkdownInContent() = runBlocking {
        val markdown = formattedEditor().toPostMarkdown()
        val methods = mutableListOf<String>()
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            methods.add(request.method)
            val buffer = okio.Buffer()
            requireNotNull(request.body).writeTo(buffer)
            val json = JsonParser.parseString(buffer.readUtf8()).asJsonObject
            assertEquals(markdown, json["content"].asString)
            val response = when (request.method) {
                "POST" -> {
                    assertEquals("/api/posts", request.url.encodedPath)
                    assertEquals(3, json["categoryId"].asInt)
                    """{"success":true,"data":15}"""
                }
                "PUT" -> {
                    assertEquals("/api/posts/15", request.url.encodedPath)
                    assertEquals(1L, json["userId"].asLong)
                    """{"id":15,"title":"수정 제목","updatedAt":"2026-10-23T12:00:00"}"""
                }
                else -> error("Unexpected request")
            }
            okhttp3.Response.Builder().request(request).protocol(Protocol.HTTP_1_1)
                .code(200).message("OK")
                .body(response.toResponseBody("application/json".toMediaType())).build()
        }.build()
        val api = Retrofit.Builder().baseUrl("https://example.test/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build().create(PostApi::class.java)
        val repository = PostRepository(api)
        assertTrue(repository.createPost(CreatePostRequest(3, "제목", markdown)).isSuccessful)
        assertTrue(repository.updatePost(15L, UpdatePostRequest(1L, "수정 제목", markdown)).isSuccessful)
        assertEquals(listOf("POST", "PUT"), methods)
    }
}
