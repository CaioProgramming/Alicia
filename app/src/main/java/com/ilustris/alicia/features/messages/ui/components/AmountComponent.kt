package com.ilustris.alicia.features.messages.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.utils.formatToCurrencyText

@Composable
fun AmountComponent(amount: Double) {

    var value by remember { mutableStateOf(0.0) }
    val valueCounter by animateFloatAsState(
        targetValue = value.toFloat(),
        animationSpec = tween(
            durationMillis = 2000,
            easing = FastOutSlowInEasing
        )
    )
    LaunchedEffect(Unit) {
        value = amount
    }

    val visible by remember {
        mutableStateOf(true).apply {
            this.value = true
        }
    }
    val infiniteTransition = rememberInfiniteTransition()

    val colors = listOf(
        Color.Transparent,
        MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
    )

    val fontsize =
        with(LocalDensity.current) { MaterialTheme.typography.headlineLarge.fontSize.toPx() }
    val fontsizeDouble = fontsize * 3

    val offsetAnimation = infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = fontsizeDouble,
        animationSpec = infiniteRepeatable(
            tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        )
    )

    val brush = Brush.linearGradient(
        colors,
        start = Offset(offsetAnimation.value, offsetAnimation.value),
        end = Offset(x = offsetAnimation.value + fontsize, y = offsetAnimation.value),
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500))
    ) {

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "R$",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W300),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
            Text(
                modifier = Modifier
                    .graphicsLayer(alpha = 0.90f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                brush,
                                blendMode = BlendMode.SrcAtop,
                            )
                        }
                    },
                text = valueCounter.toDouble().formatToCurrencyText(false),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                )
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun amountPreview() {
    AmountComponent(amount = 500.00)
}