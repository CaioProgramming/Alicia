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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.utils.formatToCurrencyText

@Composable
fun AmountComponent(amount: Double) {

    var value by remember { mutableStateOf(0.0) }
    val daysCounter by animateFloatAsState(
        targetValue = value.toFloat(),
        animationSpec = tween(
            durationMillis = 2000,
            easing = FastOutSlowInEasing
        )
    )
    LaunchedEffect(Unit) {
        value = amount
    }

    var visible by remember {
        mutableStateOf(true).apply {
            this.value = true
        }
    }
    val infiniteTransition = rememberInfiniteTransition()
    val offsetAnimation by infiniteTransition.animateValue(
        initialValue = 0.dp,
        targetValue = 25.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val colors = listOf(
        MaterialTheme.colorScheme.onBackground,
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.onBackground,
    )
    val brush = Brush.radialGradient(colors)

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
                            scale(offsetAnimation.value) {
                                drawRect(
                                    brush,
                                    blendMode = BlendMode.SrcIn,
                                )
                            }
                        }
                    },
                text = daysCounter.toDouble().formatToCurrencyText(false),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
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