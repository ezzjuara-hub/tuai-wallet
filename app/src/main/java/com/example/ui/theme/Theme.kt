package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TuaiColorScheme = lightColorScheme(
    primary = TuaiTerracotta,
    onPrimary = Color.White,
    secondary = TuaiGoldAccent,
    onSecondary = TuaiTextDark,
    tertiary = TuaiTerracottaDark,
    background = TuaiCreamBg,
    onBackground = TuaiTextDark,
    surface = Color.White,
    onSurface = TuaiTextDark,
    surfaceVariant = TuaiInputBg,
    onSurfaceVariant = TuaiTextSecondary
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TuaiColorScheme,
        typography = Typography,
        content = content
    )
}
