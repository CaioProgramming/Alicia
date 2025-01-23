package com.ilustris.alicia.core.theme

import ai.atick.material.MaterialColor
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DarkColorScheme =
    darkColorScheme(
        primary = Color(0xFF303F9F),
        secondary = Color(0xFF1A237E),
        tertiary = Color(0xFF182E2F),
        background = Color.Black,
        surface = MaterialColor.Gray800,
        onSurface = Color.White,
        onBackground = Color.White,
        primaryContainer = Color(0xFF2962FF),
        secondaryContainer = Color(0xFF283593),
        onPrimaryContainer = Color.White,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Color(0xFF2196F3),
        secondary = Color(0xFF3D5AFE),
        tertiary = Color(0xFF00B8D4),
        background = Color.White,
        surface = MaterialColor.Gray200,
        onSurface = Color.Black,
        onBackground = Color.Black,
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
     */
    )

@Composable
fun aliciaBrush() =
    Brush.verticalGradient(
        aliciaColors(),
        tileMode = TileMode.Mirror,
    )

@Composable
fun AliciaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = toolbarColor(darkTheme).toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}

fun toolbarColor(darkTheme: Boolean) = if (darkTheme) MaterialColor.Black else MaterialColor.White
