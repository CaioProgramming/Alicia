package com.ilustris.alicia.features.finnance.ui.component

import ai.atick.material.MaterialColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.data.model.TagHelper
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import java.util.*

@Composable
fun GoalMedal(goal: Goal, size: Dp, enabled: Boolean = true, onClick: () -> Unit) {
    var rotated by remember { mutableStateOf(false) }

    var direction by remember {
        mutableStateOf(180f)
    }

    val rotation by animateFloatAsState(
        targetValue = if (!rotated) direction else 0f,
        animationSpec = tween(500)
    )

    val animateFront by animateFloatAsState(
        targetValue = if (!rotated) 1f else 0f,
        animationSpec = tween(500)
    )

    val animateBack by animateFloatAsState(
        targetValue = if (rotated) 1f else 0f,
        animationSpec = tween(500)
    )

    val infiniteTransition = rememberInfiniteTransition()
    val rotationAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1500, easing = LinearEasing))
    )

    val brush = if (goal.isComplete) Brush.verticalGradient(
        listOf(
            MaterialColor.Blue900,
            MaterialColor.Blue600,
            MaterialColor.Blue300
        ),
        tileMode = TileMode.Mirror
    ) else Brush.linearGradient(
        colors = listOf(
            MaterialColor.Black,
            MaterialColor.Gray900,
            MaterialColor.Gray800,
            MaterialColor.Black
        ),
        tileMode = TileMode.Mirror
    )


    val backBrush = if (goal.isComplete) Brush.linearGradient(
        colors = listOf(
            MaterialColor.White,
            MaterialColor.Gray300,
            MaterialColor.Gray400,
            MaterialColor.Gray200,
            MaterialColor.White
        ),
        tileMode = TileMode.Mirror
    ) else brush

    val matrix = ColorMatrix().apply {
        setToSaturation(if (goal.isComplete) 1f else 0f)
    }

    val stroke = 15f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {

        Column(
            modifier = Modifier
                .wrapContentSize()
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 8 * density
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(modifier = Modifier
                .padding(4.dp)
                .size(size)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val (x, y) = dragAmount
                        if (x > 0) {
                            direction = 180f
                            //rotated = !rotated
                        } else if (x < 0) {
                            direction = -180f
                            //rotated = !rotated
                        }

                    }
                }
                .background(
                    brush = if (!rotated) brush else backBrush,
                    CircleShape,
                    alpha = 0.8f
                )
                .drawBehind {
                    if (goal.isComplete) {
                        rotate(rotationAnimation.value) {
                            drawCircle(
                                if (!rotated) brush else backBrush,
                                style = Stroke(stroke)
                            )
                        }
                    } else {
                        drawCircle(if (!rotated) brush else backBrush, style = Stroke(stroke))
                    }


                }
                .clip(CircleShape)
                .clickable {
                    if (enabled) {
                        rotated = !rotated
                    }
                    onClick()
                }
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 8 * density
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (!rotated) {
                    Image(
                        painterResource(id = TagHelper.findBadgeResource(goal.badge, goal.tag)),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.colorMatrix(matrix),
                        modifier = Modifier
                            .fillMaxSize(fraction = 0.9f)
                            .clip(CircleShape)

                    )
                } else {
                    val formattedDate = Calendar.getInstance().apply {
                        timeInMillis = if (goal.isComplete) goal.completedAt else goal.createdAt
                    }.time.format(DateFormats.DD_OF_MM)
                    val message =
                        if (goal.isComplete) "Conquistado em\n$formattedDate" else "Criado em\n$formattedDate"
                    Text(
                        text = message.uppercase(Locale.getDefault()),
                        modifier = Modifier
                            .graphicsLayer {
                                alpha = if (!rotated) animateFront else animateBack
                            }
                            .padding(8.dp),
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialColor.White),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Text(
            text = goal.description,
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GoalPreview() {
    AliciaTheme {
        Column {
            GoalMedal(
                Goal(
                    description = "Honda Civic",
                    tag = Tag.TRANSPORT,
                    value = 50000.00,
                    createdAt = Calendar.getInstance().timeInMillis
                ),
                size = 100.dp
            ) {}
            GoalMedal(
                Goal(
                    description = "Monitor",
                    tag = Tag.SHOPPING,
                    value = 500.00,
                    isComplete = true,
                    createdAt = Calendar.getInstance().timeInMillis
                ),
                size = 100.dp,
                enabled = false
            ) {}
        }

    }
}