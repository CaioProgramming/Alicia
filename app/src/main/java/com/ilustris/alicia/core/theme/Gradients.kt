package com.ilustris.alicia.core.theme

import ai.atick.material.MaterialColor
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

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
fun backGroundBrush() =
    Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surface.copy(alpha = .50f),
            MaterialTheme.colorScheme.background,
        ),
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
