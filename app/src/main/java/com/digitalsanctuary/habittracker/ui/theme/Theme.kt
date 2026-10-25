package com.digitalsanctuary.habittracker.ui.theme

import android.app.Activity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary            = PrimaryBlue,
    onPrimary          = Color.White,
    primaryContainer   = AccentBlueLight,
    onPrimaryContainer = PrimaryBlueDark,
    secondary          = AccentGreen,
    onSecondary        = Color.White,
    secondaryContainer = AccentGreenLight,
    background         = BackgroundLight,
    onBackground       = TextPrimary,
    surface            = SurfaceWhite,
    onSurface          = TextPrimary,
    surfaceVariant     = Color(0xFFF0F4F8),
    onSurfaceVariant   = TextSecondary,
    error              = ErrorRed,
    outline            = DividerColor,
)

private val DarkColors = darkColorScheme(
    primary            = PrimaryBlueLight,
    onPrimary          = Color.White,
    primaryContainer   = PrimaryBlueDark,
    onPrimaryContainer = AccentBlueLight,
    secondary          = AccentGreen,
    onSecondary        = Color.Black,
    secondaryContainer = Color(0xFF1A4D37),
    background         = DarkBackground,
    onBackground       = DarkTextPrimary,
    surface            = DarkSurface,
    onSurface          = DarkTextPrimary,
    surfaceVariant     = DarkSurface2,
    onSurfaceVariant   = DarkTextSecondary,
    error              = Color(0xFFEF9A9A),
    outline            = Color(0xFF2E4055),
)

@Composable
fun HabitTrackerTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
