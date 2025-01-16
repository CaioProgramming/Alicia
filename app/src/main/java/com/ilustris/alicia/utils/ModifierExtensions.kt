package com.ilustris.alicia.utils

import android.graphics.Paint
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@Composable
fun Modifier.gradientAnimation(
    colors: List<Color>,
    duration: Duration = 2.seconds,
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
        .graphicsLayer(alpha = 0.99f)
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

@Composable
fun Modifier.gradientFill(brush: Brush) =
    this
        .graphicsLayer(alpha = 0.99f)
        .drawWithCache {
            onDrawWithContent {
                drawContent()
                drawRect(
                    brush,
                    blendMode = BlendMode.SrcAtop,
                )
            }
        }

@Composable
fun Modifier.glow(
    glowingColor: Color,
    containerColor: Color = Color.White,
    cornersRadius: Dp = 0.dp,
    glowingRadius: Dp = 20.dp,
    xShifting: Dp = 0.dp,
    yShifting: Dp = 0.dp
) : Modifier {
    return this
        .drawBehind {
            val canvasSize = size
            drawContext.canvas.nativeCanvas.apply {
                drawRoundRect(
                    0f, // Left
                    0f, // Top
                    canvasSize.width, // Right
                    canvasSize.height, // Bottom
                    cornersRadius.toPx(), // Radius X
                    cornersRadius.toPx(), // Radius Y
                    Paint().apply {
                        color = containerColor.toArgb()
                        isAntiAlias = true
                        setShadowLayer(
                            glowingRadius.toPx(),
                            xShifting.toPx(), yShifting.toPx(),
                            glowingColor.copy(alpha = 0.85f).toArgb()
                        )
                    }
                )
            }
        }
}


