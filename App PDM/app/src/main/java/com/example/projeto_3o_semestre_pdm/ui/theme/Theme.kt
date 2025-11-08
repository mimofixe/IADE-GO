package com.example.projeto_3o_semestre_pdm.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = AccentTeal,
    onPrimary = BackgroundWhite,
    primaryContainer = AccentTealDark,
    secondary = PrimaryBlue,
    onSecondary = BackgroundWhite,
    background = BackgroundLight,
    onBackground = TextDark,
    surface = BackgroundWhite,
    onSurface = TextDark,
    error = CoralAccent
)

private val DarkColorScheme = darkColorScheme(
    primary = AccentTeal,
    onPrimary = BackgroundWhite,
    primaryContainer = AccentTealDark,
    secondary = PrimaryBlue,
    onSecondary = BackgroundWhite,
    background = Color(0xFF1A1A1A),
    onBackground = BackgroundWhite,
    surface = Color(0xFF2A2A2A),
    onSurface = BackgroundWhite,
    error = CoralAccent
)

@Composable
fun IADEGOTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}