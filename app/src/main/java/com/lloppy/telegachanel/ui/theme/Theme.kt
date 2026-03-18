package com.lloppy.telegachanel.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkRazumPrimary,
    onPrimary = Color.White,
    primaryContainer = DarkRazumContainer,
    onPrimaryContainer = DarkTextPrimary,
    secondary = DarkDushaPrimary,
    onSecondary = Color.White,
    secondaryContainer = DarkDushaContainer,
    onSecondaryContainer = DarkTextPrimary,
    tertiary = DarkTeloPrimary,
    onTertiary = Color.White,
    tertiaryContainer = DarkTeloContainer,
    onTertiaryContainer = DarkTextPrimary,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    error = DarkError,
    onError = Color.White,
    outline = DarkTextHint,
    outlineVariant = DarkSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = LightRazumPrimary,
    onPrimary = Color.White,
    primaryContainer = LightRazumContainer,
    onPrimaryContainer = LightTextPrimary,
    secondary = LightDushaPrimary,
    onSecondary = Color.White,
    secondaryContainer = LightDushaContainer,
    onSecondaryContainer = LightTextPrimary,
    tertiary = LightTeloPrimary,
    onTertiary = Color.White,
    tertiaryContainer = LightTeloContainer,
    onTertiaryContainer = LightTextPrimary,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    error = LightError,
    onError = Color.White,
    outline = LightTextHint,
    outlineVariant = LightSurfaceVariant
)

@Composable
fun TelegaChanelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val spaceColors = if (darkTheme) DarkSpaceColors else LightSpaceColors

    CompositionLocalProvider(LocalSpaceColors provides spaceColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object SpaceTheme {
    val colors: SpaceColors
        @Composable get() = LocalSpaceColors.current
}
