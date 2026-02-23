package com.shantjap.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = NeutralPrimary,
    onPrimary = Color.White,
    background = NeutralBackground,
    onBackground = NeutralOnBackground,
    surface = NeutralSurface,
    onSurface = NeutralOnSurface
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFEAE6E0),
    onPrimary = Color(0xFF1B1B1B),
    background = Color(0xFF12110F),
    onBackground = Color(0xFFEAE6E0),
    surface = Color(0xFF1B1916),
    onSurface = Color(0xFFEAE6E0)
)

@Composable
fun ShantJapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
