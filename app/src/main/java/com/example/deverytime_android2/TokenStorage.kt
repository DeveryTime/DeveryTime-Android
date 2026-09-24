package com.example.deverytime_android2

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object TokenStorage {
    private const val PREFERENCES_NAME = "authentication"
    private const val ACCESS_TOKEN_KEY = "access_token"
    private const val REFRESH_TOKEN_KEY = "refresh_token"

    private const val KEY_ALIAS = "deverytime_token_key"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val ENCRYPTED_PREFIX = "v1"
    private const val GCM_TAG_LENGTH = 128

    private lateinit var preferences: SharedPreferences

    private val _sessionExpired = MutableStateFlow(false)
    val sessionExpired = _sessionExpired.asStateFlow()

    fun initialize(context: Context) {
        preferences =
            context.applicationContext.getSharedPreferences(
                PREFERENCES_NAME,
                Context.MODE_PRIVATE,
            )

        migratePlaintextTokens()
    }

    fun saveTokens(tokens: TokenData) {
        preferences.edit()
            .putString(
                ACCESS_TOKEN_KEY,
                encrypt(tokens.accessToken),
            )
            .putString(
                REFRESH_TOKEN_KEY,
                encrypt(tokens.refreshToken),
            )
            .apply()

        _sessionExpired.value = false
    }

    fun getAccessToken(): String? {
        return readToken(ACCESS_TOKEN_KEY)
    }

    fun getRefreshToken(): String? {
        return readToken(REFRESH_TOKEN_KEY)
    }

    fun clear() {
        preferences.edit()
            .remove(ACCESS_TOKEN_KEY)
            .remove(REFRESH_TOKEN_KEY)
            .apply()

        _sessionExpired.value = true
    }

    fun resetSessionExpired() {
        _sessionExpired.value = false
    }

    private fun readToken(key: String): String? {
        val encryptedToken =
            preferences.getString(key, null)
                ?: return null

        return runCatching {
            decrypt(encryptedToken)
        }.getOrElse {
            clear()
            null
        }
    }

    private fun encrypt(value: String): String {
        val cipher =
            Cipher.getInstance(TRANSFORMATION)

        cipher.init(
            Cipher.ENCRYPT_MODE,
            getOrCreateSecretKey(),
        )

        val encryptedBytes =
            cipher.doFinal(
                value.toByteArray(Charsets.UTF_8),
            )

        val iv =
            Base64.encodeToString(
                cipher.iv,
                Base64.NO_WRAP,
            )

        val encryptedValue =
            Base64.encodeToString(
                encryptedBytes,
                Base64.NO_WRAP,
            )

        return "$ENCRYPTED_PREFIX:$iv:$encryptedValue"
    }

    private fun decrypt(value: String): String {
        val parts =
            value.split(
                ':',
                limit = 3,
            )

        require(
            parts.size == 3 &&
                    parts[0] == ENCRYPTED_PREFIX,
        )

        val iv =
            Base64.decode(
                parts[1],
                Base64.NO_WRAP,
            )

        val encryptedBytes =
            Base64.decode(
                parts[2],
                Base64.NO_WRAP,
            )

        val cipher =
            Cipher.getInstance(TRANSFORMATION)

        cipher.init(
            Cipher.DECRYPT_MODE,
            getOrCreateSecretKey(),
            GCMParameterSpec(
                GCM_TAG_LENGTH,
                iv,
            ),
        )

        return cipher
            .doFinal(encryptedBytes)
            .toString(Charsets.UTF_8)
    }

    @Synchronized
    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore =
            KeyStore
                .getInstance(ANDROID_KEYSTORE)
                .apply {
                    load(null)
                }

        val existingKey =
            keyStore.getKey(
                KEY_ALIAS,
                null,
            ) as? SecretKey

        if (existingKey != null) {
            return existingKey
        }

        val keyGenerator =
            KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE,
            )

        val keySpec =
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or
                        KeyProperties.PURPOSE_DECRYPT,
            )
                .setBlockModes(
                    KeyProperties.BLOCK_MODE_GCM,
                )
                .setEncryptionPaddings(
                    KeyProperties.ENCRYPTION_PADDING_NONE,
                )
                .setKeySize(256)
                .build()

        keyGenerator.init(keySpec)

        return keyGenerator.generateKey()
    }

    private fun migratePlaintextTokens() {
        val accessToken =
            preferences.getString(
                ACCESS_TOKEN_KEY,
                null,
            )

        val refreshToken =
            preferences.getString(
                REFRESH_TOKEN_KEY,
                null,
            )

        val alreadyEncrypted =
            accessToken
                ?.startsWith("$ENCRYPTED_PREFIX:") == true &&
                    refreshToken
                        ?.startsWith("$ENCRYPTED_PREFIX:") == true

        if (
            !alreadyEncrypted &&
            accessToken != null &&
            refreshToken != null
        ) {
            saveTokens(
                TokenData(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                ),
            )
        }
    }
}