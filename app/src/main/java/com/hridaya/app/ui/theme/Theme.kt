package com.hridaya.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Hridaya Palette — warm sandalwood, deep indigo, lotus pink
val Sandalwood = Color(0xFFE8C9A0)
val DeepIndigo = Color(0xFF1A1040)
val LotusRose = Color(0xFFE07A90)
val SoftCream = Color(0xFFFDF6EC)
val AshGray = Color(0xFF8A8499)
val MidnightBlue = Color(0xFF0F0A24)
val GlowAmber = Color(0xFFFFB347)
val TealMist = Color(0xFF7ECECA)
val SoftLilac = Color(0xFFC9B8E8)

private val DarkColorScheme = darkColorScheme(
    primary = LotusRose,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF5C1A2E),
    onPrimaryContainer = Color(0xFFFFD0D8),
    secondary = Sandalwood,
    onSecondary = DeepIndigo,
    secondaryContainer = Color(0xFF3D2A10),
    onSecondaryContainer = Sandalwood,
    tertiary = TealMist,
    onTertiary = MidnightBlue,
    background = MidnightBlue,
    onBackground = SoftCream,
    surface = Color(0xFF160E35),
    onSurface = SoftCream,
    surfaceVariant = Color(0xFF231748),
    onSurfaceVariant = AshGray,
    outline = Color(0xFF3A2E5A),
    error = Color(0xFFFF6B6B),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFB5335A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD0D8),
    onPrimaryContainer = Color(0xFF3A0020),
    secondary = Color(0xFF8B6A3E),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDDB3),
    onSecondaryContainer = Color(0xFF2B1700),
    tertiary = Color(0xFF3D7E7E),
    onTertiary = Color.White,
    background = SoftCream,
    onBackground = DeepIndigo,
    surface = Color.White,
    onSurface = DeepIndigo,
    surfaceVariant = Color(0xFFF0EAF8),
    onSurfaceVariant = Color(0xFF4A4060),
    outline = Color(0xFFB0A8C8),
    error = Color(0xFFCC3333),
)

@Composable
fun HridayaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = HridayaTypography,
        content = content
    )
}
