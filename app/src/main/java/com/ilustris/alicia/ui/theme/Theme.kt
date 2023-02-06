package com.ilustris.alicia.ui.theme

import ai.atick.material.MaterialColor
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = MaterialColor.Blue800,
    secondary = MaterialColor.Blue800,
    tertiary = MaterialColor.BlueA100,
    background = MaterialColor.Black,
    surface = MaterialColor.Gray900,
    secondaryContainer = MaterialColor.Blue800.copy(alpha = 0.2f),
    onPrimary = MaterialColor.White,
)

private val LightColorScheme = lightColorScheme(
    primary = MaterialColor.Blue500,
    secondary = MaterialColor.Blue800,
    tertiary = MaterialColor.BlueA100,
    background = MaterialColor.White,
    surface = MaterialColor.Gray500,
    secondaryContainer = MaterialColor.Blue500.copy(alpha = 0.2f),


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
fun AliciaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
        content = content
    )
}


fun toolbarColor(darkTheme: Boolean) = if (darkTheme) MaterialColor.Black else MaterialColor.White


