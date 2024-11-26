package io.hvk.noteappdel.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9C27B0),        // Deep Purple
    secondary = Color(0xFFE040FB),       // Purple Accent
    tertiary = Color(0xFFAB47BC),        // Light Purple
    surface = Color(0xFF1C1C1E),
    surfaceVariant = Color(0xFF2C2C2E),
    background = Color(0xFF000000),
    scrim = Color(0xFF000000),
    onSurface = Color(0xFFFFFFFF),
    onSurfaceVariant = Color(0xFFEBEBF5).copy(alpha = 0.6f),
    error = Color(0xFFFF3131),
    primaryContainer = Color(0xFF7B1FA2), // Darker Purple for containers
    onPrimaryContainer = Color(0xFFFFFFFF)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6A1B9A),        // Purple
    secondary = Color(0xFFAB47BC),       // Light Purple
    tertiary = Color(0xFF8E24AA),        // Medium Purple
    surface = Color(0xFFF2F2F7),
    surfaceVariant = Color(0xFFFFFFFF),
    background = Color(0xFFF2F2F7),
    scrim = Color(0xFF1A1A1A),
    onSurface = Color(0xFF000000),
    onSurfaceVariant = Color(0xFF3C3C43).copy(alpha = 0.6f),
    error = Color(0xFFFF0000),
    primaryContainer = Color(0xFF9C27B0), // Deep Purple for containers
    onPrimaryContainer = Color(0xFFFFFFFF)
)

@Composable
fun NoteAppDelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}