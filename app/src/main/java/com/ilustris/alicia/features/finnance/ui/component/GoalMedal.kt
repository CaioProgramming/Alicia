package com.ilustris.alicia.features.finnance.ui.component

import ai.atick.material.MaterialColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import java.util.*

@Composable
fun GoalMedal(goal: Goal) {
    var rotated by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (!rotated) 180f else 0f,
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
        )
    ) else Brush.linearGradient(
        colors = listOf(
            MaterialColor.Gray900,
            MaterialColor.Gray300,
            MaterialColor.Gray100
        )
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
    ) {
        val strokeWidth = 200f
        Column(modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .drawBehind {
                if (goal.isComplete) {
                    rotate(rotationAnimation.value) {
                        drawCircle(brush, style = Stroke(strokeWidth))
                    }
                } else {
                    drawCircle(brush, style = Stroke(strokeWidth))
                }

            }
            .clickable {
                rotated = !rotated
            }
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .background(
                        color = if (!rotated) Color.Transparent else MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
                    .graphicsLayer {
                        cameraDistance = 8 * density
                        alpha = if (rotated) animateBack else animateFront
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                val goalDate = Calendar.getInstance().apply {
                    timeInMillis = if (goal.isComplete) goal.completedAt else goal.createdAt
                }.time.format(DateFormats.DD_OF_MM)
                val message = if (goal.isComplete) "Conquistado em" else "Criada em"
                val text = "$message $goalDate"

                val textStyle = if (!rotated) MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                ) else MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(6f, TextUnitType.Sp)
                )

                Text(
                    text = if (!rotated) goal.tag.emoji else text,
                    style = textStyle,
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = if (rotated) animateBack else animateFront
                            rotationY = rotation
                            cameraDistance = 8 * density
                        }
                        .padding(8.dp)
                )
            }

        }

        Text(
            text = goal.description,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.W500,
                fontSize = TextUnit(10f, TextUnitType.Sp)
            )
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
                )
            )
            GoalMedal(
                Goal(
                    description = "Monitor",
                    tag = Tag.SHOPPING,
                    value = 500.00,
                    isComplete = true,
                    createdAt = Calendar.getInstance().timeInMillis
                )
            )
        }

    }
}