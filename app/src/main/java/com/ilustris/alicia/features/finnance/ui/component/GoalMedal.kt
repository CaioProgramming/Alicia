package com.ilustris.alicia.features.finnance.ui.component

import ai.atick.material.MaterialColor
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.material.curvedText
import com.ilustris.alicia.core.theme.AliciaTheme
import com.ilustris.alicia.core.theme.backGroundBrush
import com.ilustris.alicia.core.theme.toolbarColor
import com.ilustris.alicia.features.finnance.data.model.BadgeForTag
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.data.model.TagHelper
import com.ilustris.alicia.features.finnance.data.model.findTag
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import java.util.*

@Composable
fun GoalMedal(
    goal: Goal,
    preffSize: Dp,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    @Composable
    fun getBrushColors(goal: Goal): List<Color> {
        val contentColor =
            if (isSystemInDarkTheme()) MaterialColor.Gray900 else MaterialColor.Gray600

        return if (goal.isComplete) {
            listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            )
        } else {
            listOf(
                contentColor,
                contentColor.copy(0.5f),
                contentColor.copy(alpha = 0.1f),
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier.padding(8.dp),
    ) {
        var rotated by remember { mutableStateOf(false) }

        var direction by remember {
            mutableStateOf(180f)
        }

        val rotation by animateFloatAsState(
            targetValue = if (!rotated) direction else 0f,
            animationSpec = tween(1000),
        )

        val animateFront by animateFloatAsState(
            targetValue = if (rotated) 0f else 0.9f,
            animationSpec = tween(1700),
        )

        val animateBack by animateFloatAsState(
            targetValue = if (rotated) 1f else 0f,
            animationSpec = tween(1700),
        )
        val infiniteTransition = rememberInfiniteTransition()

        val fontsize =
            with(LocalDensity.current) {
                MaterialTheme.typography.displaySmall.fontSize
                    .toPx()
            }
        val fontsizeDouble = fontsize * 2

        val offsetAnimation =
            infiniteTransition.animateFloat(
                initialValue = if (goal.isComplete) 0f else 100f,
                targetValue = if (goal.isComplete) fontsizeDouble else 100f,
                animationSpec =
                    infiniteRepeatable(
                        tween(1500, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse,
                    ),
            )

        val brush =
            Brush.linearGradient(
                getBrushColors(goal = goal),
                start = Offset(offsetAnimation.value, offsetAnimation.value),
                end = Offset(x = offsetAnimation.value + fontsizeDouble, y = offsetAnimation.value),
                tileMode = TileMode.Mirror,
            )

        Column(
            modifier =
                Modifier
                    .wrapContentSize()
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 8 * density
                    },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Column(
                modifier =
                    Modifier
                        .size(preffSize)
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                val (x, y) = dragAmount
                                if (x > 0) {
                                    direction = 180f
                                    // rotated = !rotated
                                } else if (x < 0) {
                                    direction = -180f
                                    // rotated = !rotated
                                }
                            }
                        }.background(
                            color = toolbarColor(isSystemInDarkTheme()),
                            CircleShape,
                        ).border(preffSize / 10, brush, CircleShape)
                        .clip(CircleShape)
                        .clickable {
                            if (enabled) {
                                rotated = !rotated
                            }
                            onClick()
                        }.graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 8 * density
                        },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (!rotated) {
                    val icon =
                        remember {
                            TagHelper
                                .findBadgeResource(goal.badge, goal.tag.findTag())
                        }
                    Image(
                        painterResource(id = icon),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier =
                            Modifier
                                .fillMaxSize(0.5f)
                                .graphicsLayer {
                                    alpha = animateFront
                                }.drawWithCache {
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(
                                            brush,
                                            blendMode = BlendMode.SrcAtop,
                                        )
                                    }
                                },
                    )
                } else {
                    val formattedDate =
                        Calendar
                            .getInstance()
                            .apply {
                                timeInMillis =
                                    if (goal.isComplete) goal.completedAt else goal.createdAt
                            }.time
                            .format(DateFormats.DD_OF_MM)
                    val message =
                        if (goal.isComplete) "Conquistado em\n$formattedDate" else "Criado em\n$formattedDate"
                    Text(
                        text = message.uppercase(Locale.getDefault()),
                        modifier =
                            Modifier
                                .graphicsLayer {
                                    alpha = animateBack
                                }.padding(8.dp),
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onBackground),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        Text(
            text = goal.name,
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
fun GoalPreview() {
    AliciaTheme {
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(Tag.entries.reversed()) { tag ->
                GoalMedalV3(
                    modifier =
                        Modifier
                            .size(200.dp)
                            .padding(4.dp),
                    showText = true,
                    isAnimated = true,
                    goal =
                        Goal(
                            name = tag.description,
                            tag = tag.name,
                            badge = TagHelper.getRandomBadgeForTag(tag),
                            value = Random().nextDouble(),
                            createdAt = Calendar.getInstance().timeInMillis,
                        ),
                )
            }
        }
        /*GoalMedalV3(
            Goal(
                0,
                10000.0,
                "Fisioterapia",
                tag = Tag.HEALTH.name,
                badge = TagHelper.getRandomBadgeForTag(Tag.HEALTH),
                createdAt = Calendar.getInstance().timeInMillis
            ),
            showText = true,
            isAnimated = true,
            modifier = Modifier.padding(16.dp).size(300.dp),
        )*/
    }
}

@Composable
fun GoalMedalV2(
    goal: Goal,
    showText: Boolean = true,
    isAnimated: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val tag = remember { goal.tag.findTag() }
    val icon = remember { TagHelper.findBadgeResource(goal.badge, tag) }
    val backgroundBrush = backGroundBrush()
    val brush = tag.tagGradient(isAnimated = isAnimated)

    val clip =
        remember {
            TagHelper.tagShape(tag)
        }

    val textColor =
        MaterialTheme.colorScheme.onBackground.copy(
            alpha = .7f,
        )
    val textStyle =
        MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 10.5.sp,
        )

    val smallTextStyle =
        MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 10.5.sp,
        )

    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier
                .border(3.dp, MaterialTheme.colorScheme.onBackground, clip)
                .background(tag.colors.last(), clip),
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = goal.name,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .padding(4.dp)
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .clip(clip)
                        .border(10.dp, brush, clip),
            )

            if (showText) {
                CurvedLayout(
                    modifier =
                        Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                ) {
                    curvedText(
                        text = goal.name.uppercase(Locale.getDefault()),
                        style = CurvedTextStyle(textStyle),
                        color = tag.textColor,
                    )
                }

                CurvedLayout(
                    modifier =
                        Modifier
                            .padding(vertical = 18.dp)
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                    anchor = 90f,
                ) {
                    curvedText(
                        text = tag.description.uppercase(Locale.getDefault()),
                        color = textColor,
                        style =
                            CurvedTextStyle(style = smallTextStyle),
                    )
                }
            }
        }

        Text(
            goal.name,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
fun GoalMedalV3(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BadgeForTag(
            goal,
            showText,
            isAnimated,
            modifier,
        )
        AnimatedVisibility(showText) {
            Text(goal.name, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
