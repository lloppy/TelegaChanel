package com.lloppy.telegachanel.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class SpaceColors(
    // Razum / Mind
    val razumPrimary: Color,
    val razumContainer: Color,
    val razumBubble: Color,

    // Dusha / Soul
    val dushaPrimary: Color,
    val dushaContainer: Color,
    val dushaBubble: Color,

    // Telo / Body
    val teloPrimary: Color,
    val teloContainer: Color,
    val teloAccent: Color,

    // Text
    val textSecondary: Color,
    val textHint: Color
)

val DarkSpaceColors = SpaceColors(
    razumPrimary = DarkRazumPrimary,
    razumContainer = DarkRazumContainer,
    razumBubble = DarkRazumBubble,
    dushaPrimary = DarkDushaPrimary,
    dushaContainer = DarkDushaContainer,
    dushaBubble = DarkDushaBubble,
    teloPrimary = DarkTeloPrimary,
    teloContainer = DarkTeloContainer,
    teloAccent = DarkTeloAccent,
    textSecondary = DarkTextSecondary,
    textHint = DarkTextHint
)

val LightSpaceColors = SpaceColors(
    razumPrimary = LightRazumPrimary,
    razumContainer = LightRazumContainer,
    razumBubble = LightRazumBubble,
    dushaPrimary = LightDushaPrimary,
    dushaContainer = LightDushaContainer,
    dushaBubble = LightDushaBubble,
    teloPrimary = LightTeloPrimary,
    teloContainer = LightTeloContainer,
    teloAccent = LightTeloAccent,
    textSecondary = LightTextSecondary,
    textHint = LightTextHint
)

val LocalSpaceColors = staticCompositionLocalOf { DarkSpaceColors }
