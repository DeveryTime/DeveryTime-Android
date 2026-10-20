package com.example.deverytime_android2

import android.content.ContentResolver
import android.net.Uri
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ProfileImageContent(
    val bytes: ByteArray,
    val mediaType: String?,
)

fun interface ProfileImageReader {
    suspend fun read(uriString: String): ProfileImageContent?
}

class ContentResolverProfileImageReader(
    private val contentResolver: ContentResolver,
) : ProfileImageReader {
    override suspend fun read(uriString: String): ProfileImageContent? = withContext(Dispatchers.IO) {
        val uri = Uri.parse(uriString)
        val mediaType = contentResolver.getType(uri)
        contentResolver.openInputStream(uri)?.use { stream ->
            ProfileImageContent(bytes = stream.readBytes(), mediaType = mediaType)
        }
    }
}

class ProfileImageReadException : IOException("프로필 이미지를 읽을 수 없습니다.")
