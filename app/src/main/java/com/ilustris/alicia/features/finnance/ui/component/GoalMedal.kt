package com.ilustris.alicia.features.finnance.ui.component

import ai.atick.material.MaterialColor
import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.data.model.TagHelper
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.toolbarColor
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import java.util.*

@Composable
fun GoalMedal(goal: Goal, size: Dp, enabled: Boolean = true, onClick: () -> Unit) {

    @Composable
    fun getBrushColors(goal: Goal): List<Color> {
        val contentColor =
            if (isSystemInDarkTheme()) MaterialColor.Gray900 else MaterialColor.Gray600

        return if (goal.isComplete) listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        ) else listOf(
            contentColor,
            contentColor.copy(0.5f),
            contentColor.copy(alpha = 0.1f)
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {

        var rotated by remember { mutableStateOf(false) }

        var direction by remember {
            mutableStateOf(180f)
        }

        val rotation by animateFloatAsState(
            targetValue = if (!rotated) direction else 0f,
            animationSpec = tween(1000)
        )

        val animateFront by animateFloatAsState(
            targetValue = if (rotated) 0f else 0.9f,
            animationSpec = tween(1700)
        )

        val animateBack by animateFloatAsState(
            targetValue = if (rotated) 1f else 0f,
            animationSpec = tween(1700)
        )
        val infiniteTransition = rememberInfiniteTransition()

        val fontsize =
            with(LocalDensity.current) { MaterialTheme.typography.displaySmall.fontSize.toPx() }
        val fontsizeDouble = fontsize * 2

        val offsetAnimation = infiniteTransition.animateFloat(
            initialValue = if (goal.isComplete) 0f else 100f,
            targetValue = if (goal.isComplete) fontsizeDouble else 100f,
            animationSpec = infiniteRepeatable(
                tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            )
        )


        val brush = Brush.linearGradient(
            getBrushColors(goal = goal),
            start = Offset(offsetAnimation.value, offsetAnimation.value),
            end = Offset(x = offsetAnimation.value + fontsizeDouble, y = offsetAnimation.value),
            tileMode = TileMode.Mirror
        )

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
            Column(
                modifier = Modifier
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
                        color = toolbarColor(isSystemInDarkTheme()),
                        CircleShape,
                    )
                    .border(5.dp, brush, CircleShape)
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
                        modifier = Modifier
                            .fillMaxSize(0.5f)
                            .graphicsLayer {
                                alpha = animateFront
                            }
                            .drawWithCache {
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(
                                        brush,
                                        blendMode = BlendMode.SrcAtop,
                                    )
                                }
                            }
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
                                alpha = animateBack
                            }
                            .padding(8.dp),
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onBackground),
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
@Composable
fun GoalPreview() {
    AliciaTheme {
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(Tag.values().size) {
                val tag = Tag.values()[it]
                GoalMedal(
                    goal = Goal(
                        description = tag.description,
                        tag = tag,
                        badge = TagHelper.getRandomBadge(),
                        value = Random().nextDouble(),
                        isComplete = it % 2 != 0,
                        createdAt = Calendar.getInstance().timeInMillis
                    ), size = 100.dp, it % 2 == 0
                ) {
                }
            }
        }
    }
}


