package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// NEXFAR Official Brand ColorScheme
// Tailored for high-contrast clarity: Navy (#142D43), Teal (#20A89A), Ice (#F4F7FA), White (#FFFFFF)
private val NexfarColorScheme = lightColorScheme(
    primary = NexfarNavy,
    onPrimary = Color.White,
    primaryContainer = NexfarTealLight,
    onPrimaryContainer = NexfarNavy,
    secondary = NexfarTeal,
    onSecondary = Color.White,
    secondaryContainer = NexfarTealLight,
    onSecondaryContainer = NexfarTealDark,
    tertiary = NexfarTealDark,
    background = NexfarIceBg,
    onBackground = NexfarTextPrimary,
    surface = NexfarSurfaceWhite,
    onSurface = NexfarTextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = NexfarTextSecondary,
    outline = NexfarCardBorder
)

@Composable
fun NexfarTheme(
    content: @Composable () -> Unit
) {
    // Enforce the official NEXFAR brand colors with optimal dark-text-on-light-surface contrast
    MaterialTheme(
        colorScheme = NexfarColorScheme,
        typography = Typography,
        content = content
    )
}
