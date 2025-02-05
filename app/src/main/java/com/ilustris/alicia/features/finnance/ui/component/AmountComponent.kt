package com.ilustris.alicia.features.finnance.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ilustris.alicia.utils.formatToCurrencyText

@Composable
fun AmountComponent(
    modifier: Modifier = Modifier,
    amount: Double,
) {
    val valueCounter by animateFloatAsState(
        targetValue = amount.toFloat(),
        animationSpec =
            tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing,
            ),
    )

    val infiniteTransition = rememberInfiniteTransition()

    val colors =
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.tertiary,
        )

    val fontsize =
        with(LocalDensity.current) {
            MaterialTheme.typography.headlineLarge.fontSize
                .toPx()
        }
    val fontsizeDouble = fontsize * 10

    val offsetAnimation =
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = fontsizeDouble,
            animationSpec =
                infiniteRepeatable(
                    tween(1500, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
        )

    val brush =
        Brush.linearGradient(
            colors,
            start = Offset(offsetAnimation.value, offsetAnimation.value),
            end = Offset(x = offsetAnimation.value + fontsizeDouble, y = offsetAnimation.value),
            tileMode = TileMode.Repeated,
        )

    Text(
        modifier =
            Modifier
                .fillMaxWidth()
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            brush,
                            blendMode = BlendMode.SrcAtop,
                        )
                    }
                },
        text = valueCounter.toDouble().formatToCurrencyText(true),
        style =
            MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            ),
    )
}

@Preview(showBackground = true)
@Composable
fun amountPreview() {
    AmountComponent(amount = 500.00)
}
