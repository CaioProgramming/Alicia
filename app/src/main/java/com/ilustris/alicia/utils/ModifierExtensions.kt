package com.ilustris.alicia.utils

import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@Composable
fun Modifier.gradientAnimation(
    colors: List<Color>,
    duration: Duration = 3.seconds,
): Modifier {
    val infiniteTransition = rememberInfiniteTransition()
    val offsetAnimation =
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 100f,
            animationSpec =
                infiniteRepeatable(
                    tween(duration.toInt(DurationUnit.MILLISECONDS), easing = EaseIn),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "Gradient Offset Animation",
        )
    val brush =
        Brush.linearGradient(
            colors = colors,
            start = Offset.Zero,
            end = Offset(offsetAnimation.value * 2, offsetAnimation.value * 3),
            tileMode = TileMode.Clamp,
        )
    return this
        .graphicsLayer(.99f)
        .drawWithCache {
            onDrawWithContent {
                drawContent()
                drawRect(brush, blendMode = BlendMode.SrcAtop)
            }
        }
}

@Composable
fun gradientAnimation(
    colors: List<Color>,
    duration: Duration = 3.seconds,
): Brush {
    val infiniteTransition = rememberInfiniteTransition()
    val offsetAnimation =
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 100f,
            animationSpec =
            infiniteRepeatable(
                tween(duration.toInt(DurationUnit.MILLISECONDS), easing = EaseIn),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "Gradient Offset Animation",
        )
    return Brush.linearGradient(
            colors = colors,
            start = Offset.Zero,
            end = Offset(offsetAnimation.value * 2, offsetAnimation.value * 3),
            tileMode = TileMode.Clamp,
        )
}
