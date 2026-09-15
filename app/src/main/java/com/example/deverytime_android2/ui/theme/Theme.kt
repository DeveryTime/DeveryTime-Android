package com.example.deverytime_android2.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.deverytime_android2.appTypography

private val darkColorSchemeColors =
    darkColorScheme(
        primary = purple80,
        secondary = purpleGrey80,
        tertiary = pink80,
    )

private val lightColorSchemeColors =
    lightColorScheme(
        primary = purple40,
        secondary = purpleGrey40,
        tertiary = pink40,
        background = Color.White,
        surface = Color.White,
    )

@Composable
fun DeveryTime_Android2Theme(
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> {
                darkColorSchemeColors
            }

            else -> {
                lightColorSchemeColors
            }
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = appTypography,
        content = content,
    )
}
