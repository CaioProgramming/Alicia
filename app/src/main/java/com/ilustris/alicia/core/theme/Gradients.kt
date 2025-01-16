package com.ilustris.alicia.core.theme

import ai.atick.material.MaterialColor
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun aliciaColors() =
    if (!isSystemInDarkTheme()) {
        listOf(
            MaterialColor.Blue300,
            MaterialColor.BlueA700,
            MaterialColor.Blue500,
        )
    } else {
        listOf(
            MaterialColor.Blue800,
            MaterialColor.BlueA200,
            MaterialColor.Blue900,
        )
    }

@Composable
fun backGroundBrush(isDarkTheme : Boolean = isSystemInDarkTheme()) =
    if (isDarkTheme)
    Brush.verticalGradient(
        listOf(
            MaterialColor.Gray700,
            MaterialColor.Gray900,
            Color.Black,
        ),
    ) else
        Brush.verticalGradient(
            listOf(
                MaterialColor.Gray100,
                MaterialColor.Gray300,
                MaterialColor.Gray50,
            )
        )



@Composable
fun themeBrush() =
    Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary,
        ),
    )
