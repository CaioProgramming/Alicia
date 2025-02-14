package com.ilustris.alicia.features.finnance.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.curvedText
import com.ilustris.alicia.R
import com.ilustris.alicia.core.theme.Flow
import com.ilustris.alicia.core.theme.HexagonShape
import com.ilustris.alicia.core.theme.Polygon
import com.ilustris.alicia.core.theme.TriangleShape
import com.ilustris.alicia.core.theme.backGroundBrush
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.TagHelper
import com.ilustris.alicia.features.finnance.data.model.findTag
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.OutLineText
import com.ilustris.alicia.utils.darker
import com.ilustris.alicia.utils.format
import com.ilustris.alicia.utils.glow
import com.ilustris.alicia.utils.gradientAnimation
import com.ilustris.alicia.utils.gradientFill
import com.ilustris.alicia.utils.toDate
import java.util.Locale

@Suppress("ktlint:standard:function-naming")
@Composable
fun HexBadge(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    val tag =
        remember {
            goal.tag.findTag()
        }

    val mainColor =
        remember {
            tag.colors.last().darker()
        }

    val shape =
        remember {
            HexagonShape()
        }

    val icon =
        remember {
            TagHelper.findBadgeResource(goal.badge, tag)
        }

    val brush = tag.tagGradient(isAnimated)

    val textStyle =
        MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Black,
            letterSpacing = 5.sp,
        )

    val smallTextStyle =
        MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 5.sp,
        )

    Box(
        modifier
            .border(3.dp, MaterialTheme.colorScheme.onBackground, shape)
            .padding(4.dp)
            .background(brush, shape)
            .clip(shape),
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = goal.name,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .border(5.dp, mainColor, shape)
                    .clip(shape),
        )
        if (showText) {
            Text(
                goal.name.uppercase(Locale.getDefault()),
                style = textStyle,
                textAlign = TextAlign.Center,
                color = Color.White,
                maxLines = 1,
                modifier =
                    Modifier
                        .offset(y = (-5).dp)
                        .background(mainColor)
                        .padding(8.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .offset(y = (-5).dp),
            )
        }
    }
}

@Composable
fun ShieldBadge(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    val tag =
        remember {
            goal.tag.findTag()
        }
    val shape =
        remember {
            RoundedCornerShape(
                topStart = 5.dp,
                topEnd = 5.dp,
                bottomStart = 50.dp,
                bottomEnd = 50.dp,
            )
        }

    val icon =
        remember {
            TagHelper.findBadgeResource(goal.badge, tag)
        }

    val mainColor =
        remember {
            tag.colors.last()
        }
    val brush = tag.tagGradient(isAnimated)

    val textStyle =
        MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 5.sp,
        )

    val smallTextStyle =
        MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 5.sp,
        )

    Column(
        modifier
            .border(3.dp, MaterialTheme.colorScheme.onBackground, shape)
            .padding(2.dp)
            .border(2.dp, brush, shape)
            .background(mainColor, shape)
            .clip(shape),
    ) {
        if (showText) {
            Text(
                goal.name.uppercase(Locale.getDefault()),
                style = textStyle,
                textAlign = TextAlign.Center,
                color = Color.White,
                maxLines = 1,
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
            )
        }

        Box {
            Image(
                painter = painterResource(id = icon),
                contentDescription = goal.name,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxSize()
                        .clip(shape),
            )

            Text(
                tag.name.uppercase(Locale.getDefault()),
                style = smallTextStyle,
                maxLines = 1,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = .5f),
                modifier =
                    Modifier
                        .wrapContentSize()
                        .background(
                            tag.colors.last().darker(0.9f),
                            RoundedCornerShape(
                                topStart = 15.dp,
                                topEnd = 15.dp,
                                bottomStart = 5.dp,
                                bottomEnd = 5.dp,
                            ),
                        ).padding(horizontal = 20.dp, vertical = 6.dp)
                        .align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
fun TriangleBadge(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    reversed: Boolean,
    modifier: Modifier,
) {
    val tag =
        remember {
            goal.tag.findTag()
        }

    val shape =
        remember {
            Polygon(3, if (reversed) 90f else -90f)
        }

    val icon =
        remember {
            TagHelper.findBadgeResource(goal.badge, tag)
        }

    val mainColor =
        remember {
            tag.colors.last()
        }
    val brush = tag.tagGradient(isAnimated)

    val textStyle =
        MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 5.sp,
        )

    val smallTextStyle =
        MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
        )

    val alignment =
        remember {
            if (reversed) Alignment.TopCenter else Alignment.BottomCenter
        }

    Box(
        modifier
            .clip(shape)
            .border(3.dp, MaterialTheme.colorScheme.onBackground, shape)
            .padding(2.dp)
            .border(4.dp, brush, shape)
            .padding(4.dp)
            .border(5.dp, mainColor, shape),
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = goal.name,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier.fillMaxSize(),
        )

        if (showText) {
            val textShape =
                if (reversed) {
                    RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
                } else {
                    RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                }

            Text(
                goal.name.uppercase(Locale.getDefault()),
                style = smallTextStyle,
                maxLines = 1,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier =
                    Modifier
                        .offset(y = if (reversed) (45).dp else (-45).dp)
                        .background(
                            mainColor,
                            textShape,
                        ).padding(8.dp)
                        .align(alignment),
            )
        }
    }
}

@Composable
fun HealthBadge(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    val tag =
        remember {
            goal.tag.findTag()
        }

    val shape =
        remember {
            Polygon(6)
        }

    val icon =
        remember {
            TagHelper.findBadgeResource(goal.badge, tag)
        }

    val mainColor =
        remember {
            tag.colors.last()
        }
    val brush = tag.tagGradient(isAnimated)

    val textStyle =
        MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Black,
        )

    val smallTextStyle =
        MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.background,
            fontWeight = FontWeight.Bold,
        )

    val alignment =
        remember { Alignment.BottomCenter }

    Box(
        modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier =
                Modifier
                    .offset(x = 20.dp, y = 20.dp)
                    .wrapContentSize()
                    .align(Alignment.TopStart),
        ) {
            Text(
                goal.name.uppercase(Locale.getDefault()),
                style =
                    textStyle.copy(
                        fontStyle = FontStyle.Italic,
                    ),
                maxLines = 1,
                textAlign = TextAlign.Start,
                modifier =
                    Modifier
                        .wrapContentSize()
                        .background(mainColor)
                        .padding(vertical = 8.dp, horizontal = 24.dp),
            )
            Text(
                tag.description,
                style =
                    smallTextStyle.copy(
                        fontStyle = FontStyle.Italic,
                    ),
                maxLines = 1,
                textAlign = TextAlign.Start,
                modifier =
                    Modifier
                        .align(Alignment.End)
                        .wrapContentSize()
                        .background(mainColor)
                        .padding(vertical = 4.dp, horizontal = 24.dp),
            )
        }

        Image(
            painter = painterResource(id = icon),
            contentDescription = goal.name,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(mainColor),
            modifier = Modifier.align(Alignment.CenterEnd).offset(x = 25.dp),
        )
    }
}

@Composable
fun PetBadge(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    val tag =
        remember {
            goal.tag.findTag()
        }

    val shape =
        remember {
            CircleShape
        }

    val icon =
        remember {
            TagHelper.findBadgeResource(goal.badge, tag)
        }

    val mainColor =
        remember {
            tag.colors.last()
        }
    val brush = tag.tagGradient(isAnimated)

    val nameStyle =
        MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Black,
            color = mainColor,
            letterSpacing = 4.sp,
            fontFamily =
                FontFamily(
                    Font(R.font.schoolbell_regular, FontWeight.Normal),
                ),
        )

    val textStyle =
        MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground,
            shadow =
                Shadow(
                    color = Color.White,
                    offset = Offset(1f, 1f),
                    blurRadius = 10f,
                ),
            letterSpacing = 4.sp,
            fontFamily =
                FontFamily(
                    Font(R.font.schoolbell_regular, FontWeight.Normal),
                ),
        )

    val smallTextStyle =
        MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .7f),
            fontWeight = FontWeight.SemiBold,
            shadow =
                Shadow(
                    color = mainColor,
                    offset = Offset(1f, 1f),
                    blurRadius = 2f,
                ),
            fontFamily =
                FontFamily(
                    Font(R.font.schoolbell_regular, FontWeight.Normal),
                ),
        )

    val alignment =
        remember { Alignment.BottomCenter }

    Box(
        modifier.clip(shape),
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = goal.name,
            contentScale = ContentScale.Fit,
            modifier =
                Modifier
                    .padding(16.dp)
                    .border(2.dp, MaterialTheme.colorScheme.onBackground, shape)
                    .clip(shape)
                    .background(brush, shape)
                    .padding(22.dp)
                    .background(tag.colors.first(), shape)
                    .clip(shape)
                    .fillMaxSize(),
        )

        AnimatedVisibility(showText, enter = scaleIn(), exit = slideOutVertically()) {
            CurvedLayout(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                curvedText(
                    tag.description.uppercase(Locale.getDefault()),
                    color = tag.textColor,
                    style = CurvedTextStyle(textStyle),
                )
            }

            CurvedLayout(modifier = Modifier.padding(16.dp).fillMaxSize(), anchor = 90f) {
                curvedText(
                    goal.createdAt.toDate().format(DateFormats.DD_OF_MM),
                    style = CurvedTextStyle(smallTextStyle),
                )
            }
        }

        Image(
            painterResource(tag.icon),
            contentDescription = "Pet",
            colorFilter = ColorFilter.tint(tag.textColor),
            modifier =
                Modifier
                    .padding(16.dp)
                    .size(24.dp)
                    .align(Alignment.CenterStart)
                    .padding(4.dp),
        )

        Image(
            painterResource(tag.icon),
            colorFilter = ColorFilter.tint(tag.textColor),
            contentDescription = "Pet",
            modifier =
                Modifier
                    .padding(16.dp)
                    .size(24.dp)
                    .align(Alignment.CenterEnd)
                    .padding(4.dp),
        )

        @Suppress("ktlint:standard:function-naming")
        AnimatedVisibility(showText, modifier = Modifier.align(Alignment.BottomCenter)) {
            OutLineText(
                text = goal.name.uppercase(Locale.getDefault()),
                width = 4.dp,
                strokeColor = MaterialTheme.colorScheme.background,
                textStyle = nameStyle,
                fontFamily = "schoolbell_regular",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun FoodBadge(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val tag =
        remember {
            goal.tag.findTag()
        }

    val shape =
        remember {
            Flow(context)
        }

    val icon =
        remember {
            TagHelper.findBadgeResource(goal.badge, tag)
        }

    val tagIcon =
        remember {
            tag.icon
        }

    val mainColor =
        remember {
            tag.colors.last()
        }
    val brush = tag.tagGradient(isAnimated)

    val textStyle =
        MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Black,
            letterSpacing = 5.sp,
        )

    val smallTextStyle =
        MaterialTheme.typography.titleSmall.copy(
            color = tag.textColor,
            fontWeight = FontWeight.Bold,
            letterSpacing = 5.sp,
        )

    Box(
        modifier
            .border(3.dp, MaterialTheme.colorScheme.onBackground, shape)
            .padding(2.dp)
            .border(4.dp, brush, shape)
            .background(mainColor, shape)
            .clip(shape),
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = goal.name,
            contentScale = ContentScale.Fit,
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .fillMaxSize()
                    .clip(shape),
        )

        AnimatedVisibility(showText, enter = scaleIn(), exit = slideOutVertically()) {
            CurvedLayout(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                curvedText(
                    goal.name.uppercase(),
                    color = textStyle.color,
                    style = CurvedTextStyle(textStyle),
                )
            }

            CurvedLayout(modifier = Modifier.padding(16.dp).fillMaxSize(), anchor = 90f) {
                curvedText(
                    goal.createdAt
                        .toDate()
                        .format(DateFormats.DD_OF_MM)
                        .uppercase(),
                    color = smallTextStyle.color,
                    style = CurvedTextStyle(smallTextStyle),
                )
            }
        }

        Image(
            painterResource(tagIcon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(textStyle.color),
            modifier =
                Modifier
                    .padding(16.dp)
                    .size(24.dp)
                    .align(Alignment.CenterStart)
                    .padding(4.dp),
        )

        Image(
            painterResource(tagIcon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(textStyle.color),
            modifier =
                Modifier
                    .padding(16.dp)
                    .size(24.dp)
                    .align(Alignment.CenterEnd)
                    .padding(4.dp),
        )
    }
}

@Composable
fun BillsBadge(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    val tag =
        remember {
            goal.tag.findTag()
        }

    val shape =
        remember {
            CircleShape
        }

    val icon =
        remember {
            TagHelper.findBadgeResource(goal.badge, tag)
        }

    val mainColor =
        remember {
            tag.colors.last()
        }
    val brush = tag.tagGradient(isAnimated)

    val textStyle =
        MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Black,
            letterSpacing = 5.sp,
            color = tag.textColor,
        )

    val smallTextStyle =
        MaterialTheme.typography.titleSmall.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            letterSpacing = 5.sp,
        )

    Box(
        modifier
            .background(MaterialTheme.colorScheme.onBackground, shape)
            .padding(4.dp)
            .border(8.dp, mainColor, shape)
            .background(brush, shape)
            .clip(shape),
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = goal.name,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(mainColor.darker(0.6f)),
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(32.dp)
                    .glow(glowingColor = Color.White, containerColor = Color.Transparent, cornersRadius = 50.dp)
                    .clip(shape)
                    .background(backGroundBrush(), shape)
                    .padding(8.dp)
                    .gradientFill(brush)
                    .clip(shape),
        )

        if (showText) {
            CurvedLayout(modifier = Modifier.fillMaxSize()) {
                curvedText(
                    goal.name.uppercase(Locale.getDefault()),
                    color = textStyle.color,
                    style = CurvedTextStyle(textStyle),
                )
            }
            CurvedLayout(modifier = Modifier.padding(8.dp).fillMaxSize(), anchor = 90f) {
                curvedText(
                    goal.createdAt
                        .toDate()
                        .format(DateFormats.DD_OF_MM)
                        .uppercase(),
                    color = textStyle.color,
                    style = CurvedTextStyle(smallTextStyle),
                )
            }
        }

        Image(
            painterResource(tag.icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(textStyle.color),
            modifier =
                Modifier
                    .padding(8.dp)
                    .size(24.dp)
                    .align(Alignment.CenterStart)
                    .padding(4.dp),
        )

        Image(
            painterResource(tag.icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(textStyle.color),
            modifier =
                Modifier
                    .padding(8.dp)
                    .size(24.dp)
                    .align(Alignment.CenterEnd)
                    .padding(4.dp),
        )
    }
}

@Composable
fun TransportBadge(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    val tag =
        remember {
            goal.tag.findTag()
        }

    val icon =
        remember {
            TagHelper.findBadgeResource(goal.badge, tag)
        }

    val mainColor =
        remember {
            tag.colors.last()
        }
    val brush = tag.tagGradient(isAnimated)

    val textStyle =
        MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Black,
            color = tag.textColor,
            fontFamily =
                FontFamily(
                    Font(R.font.goldman_bold, FontWeight.Normal),
                ),
        )

    val smallTextStyle =
        MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontFamily =
                FontFamily(
                    Font(R.font.goldman_bold, FontWeight.Normal),
                ),
        )

    Box(modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize(.5f)
                    .offset(y = 40.dp, x = 75.dp)
                    .background(brush, CircleShape)
                    .blur(25.dp)
                    .clip(CircleShape),
        )
        Image(
            painter = painterResource(id = icon),
            contentDescription = goal.name,
            contentScale = ContentScale.Fit,
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
        )
        AnimatedVisibility(showText) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Text(
                    goal.name.uppercase(Locale.getDefault()),
                    style = textStyle,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Left,
                    modifier =
                        Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                )
                Row(
                    modifier = Modifier.fillMaxWidth().align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy((-2).dp),
                ) {
                    Text(
                        tag.name.uppercase(Locale.getDefault()),
                        style = smallTextStyle,
                        color = MaterialTheme.colorScheme.background,
                        textAlign = TextAlign.End,
                        modifier =
                            Modifier
                                .background(mainColor)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                    )

                    for (i in 0..3) {
                        val align = if (i % 2 == 0) Alignment.Top else Alignment.Bottom
                        val color =
                            if (i % 2 == 0) {
                                mainColor
                            } else {
                                mainColor.darker()
                            }
                        Box(Modifier.background(color).size(12.dp).align(align)) {}
                    }

                    Image(
                        painterResource(tag.icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(mainColor.darker()),
                        modifier =
                            Modifier
                                .size(24.dp)
                                .rotate(30f),
                    )
                }
            }
        }
    }
}

@Composable
fun EducationBadge(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    val shape =
        remember {
            CircleShape
        }

    val tag =
        remember {
            goal.tag.findTag()
        }

    val icon =
        remember {
            TagHelper.findBadgeResource(goal.badge, tag)
        }

    val mainColor =
        remember {
            tag.colors.last()
        }
    val brush = tag.tagGradient(isAnimated)

    val textStyle =
        MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Black,
            color = tag.textColor,
            fontFamily =
                FontFamily(
                    Font(R.font.greek_freak, FontWeight.Normal),
                ),
        )

    val smallTextStyle =
        MaterialTheme.typography.titleSmall.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontFamily =
                FontFamily(
                    Font(R.font.greek_freak, FontWeight.Normal),
                ),
        )

    Box(
        modifier
            .border(3.dp, MaterialTheme.colorScheme.onBackground, shape)
            .background(brush, shape)
            .clip(shape),
    ) {
        Image(
            painterResource(icon),
            contentDescription = goal.name,
            contentScale = ContentScale.Fit,
            modifier =
                Modifier
                    .offset(y = 24.dp)
                    .fillMaxSize()
                    .scale(1.7f),
        )

        Image(
            painterResource(R.drawable.greek_frame),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.padding(4.dp).fillMaxSize(),
        )

        AnimatedVisibility(showText, modifier = Modifier.align(Alignment.BottomCenter)) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .border(2.dp, brush, RoundedCornerShape(0.dp))
                        .background(MaterialTheme.colorScheme.onBackground),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    goal.name,
                    maxLines = 1,
                    style = textStyle.copy(brush),
                    color = tag.textColor,
                    textAlign = TextAlign.Center,
                )

                Text(
                    tag.description,
                    maxLines = 1,
                    style = smallTextStyle.copy(brush),
                    color = tag.textColor,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun PartyBadge(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    fun fillIconResource(index: Int): Int =
        when (index) {
            0 -> R.drawable.party_badge_1_fill
            1 -> R.drawable.party_badge_2_fill
            2 -> R.drawable.party_badge_3_fill
            else -> R.drawable.ic_round_star_24
        }

    val tag =
        remember {
            goal.tag.findTag()
        }

    val shape =
        remember {
            CircleShape
        }

    val icon =
        remember {
            TagHelper.findBadgeResource(goal.badge, tag)
        }

    val fillIcon =
        remember {
            fillIconResource(goal.badge)
        }

    val mainColor =
        remember {
            tag.colors.last()
        }
    val brush = tag.tagGradient(isAnimated)

    val textStyle =
        MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Black,
            letterSpacing = 5.sp,
            brush = brush,
        )

    val smallTextStyle =
        MaterialTheme.typography.titleSmall.copy(
            brush = brush,
            fontWeight = FontWeight.Black,
            letterSpacing = 5.sp,
        )

    val alignment =
        remember { Alignment.BottomCenter }

    Box(modifier) {
        if (showText) {
            CurvedLayout(modifier = Modifier.gradientFill(brush)) {
                curvedText(
                    goal.name.uppercase(Locale.getDefault()),
                    style = CurvedTextStyle(textStyle),
                )
            }

            CurvedLayout(modifier = Modifier.align(Alignment.BottomCenter).gradientFill(brush), anchor = 90f) {
                curvedText(
                    tag.description.uppercase(Locale.getDefault()),
                    style = CurvedTextStyle(smallTextStyle),
                )
            }
        }
        Image(
            painter = painterResource(id = fillIcon),
            contentDescription = goal.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(.8f).gradientFill(brush).align(Alignment.Center),
        )

        Image(
            painter = painterResource(id = icon),
            colorFilter = ColorFilter.tint(Color.Black),
            contentDescription = goal.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(.75f).align(Alignment.Center),
        )

        Image(
            painterResource(tag.icon),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterEnd).size(20.dp).gradientFill(brush),
        )

        Image(
            painterResource(tag.icon),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterStart).size(20.dp).gradientFill(brush),
        )
    }
}

@Composable
fun TravelBadge(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    val tag =
        remember {
            goal.tag.findTag()
        }
    val brush = tag.tagGradient(isAnimated)

    val icon =
        remember {
            TagHelper.findBadgeResource(goal.badge, tag)
        }

    val mainColor =
        remember {
            tag.colors.last()
        }

    val textStyle =
        MaterialTheme.typography.displaySmall.copy(
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            fontFamily =
                FontFamily(
                    Font(R.font.lobster_regular, FontWeight.Normal),
                ),
        )

    Box(modifier) {
        Box(
            Modifier
                .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                .padding(2.dp)
                .border(3.dp, brush, CircleShape)
                .fillMaxSize(.85f)
                .align(Alignment.Center)
                .clip(CircleShape),
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = goal.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

            AnimatedVisibility(showText, Modifier.align(Alignment.BottomCenter)) {
                Text(
                    tag.description.uppercase(Locale.getDefault()),
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .6f),
                            letterSpacing = 3.sp,
                        ),
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(
                                brush =
                                    Brush.verticalGradient(
                                        colors =
                                            listOf(
                                                mainColor.copy(0f),
                                                mainColor.copy(.3f),
                                                mainColor.copy(.6f),
                                                mainColor.copy(.7f),
                                                mainColor,
                                            ),
                                    ),
                            ).padding(16.dp),
                )
            }
        }

        val textScale = 1.6f
        val textModifier = Modifier.align(Alignment.Center).scale(textScale).rotate(-15f)

        AnimatedVisibility(showText, modifier = Modifier.align(Alignment.Center)) {
            Text(
                goal.name,
                maxLines = 1,
                style = textStyle.copy(brush = brush),
                textAlign = TextAlign.Center,
                modifier = textModifier.gradientFill(brush).offset((-2).dp, 3.dp),
            )

            Text(
                goal.name,
                maxLines = 1,
                style = textStyle,
                textAlign = TextAlign.Center,
                modifier = textModifier,
            )
        }
    }
}

@Composable
fun ShoppingBadge(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    val tag =
        remember {
            goal.tag.findTag()
        }

    val shape =
        remember {
            CircleShape
        }

    val icon =
        remember {
            TagHelper.findBadgeResource(goal.badge, tag)
        }

    val mainColor =
        remember {
            tag.colors.last()
        }
    val brush = tag.tagGradient(isAnimated)

    val textStyle =
        MaterialTheme.typography.headlineLarge.copy(
            letterSpacing = 0.sp,
            fontFamily = FontFamily(Font(R.font.delta_phoenix, FontWeight.Normal)),
            color = tag.textColor,
        )

    val smallTextStyle =
        MaterialTheme.typography.titleSmall.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            letterSpacing = 5.sp,
        )

    Box(
        modifier,
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = goal.name,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxSize(.75f)
                    .gradientAnimation(tag.colors),
        )
        Image(
            painter = painterResource(id = icon),
            contentDescription = goal.name,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier =
                Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterStart)
                    .fillMaxSize(.75f),
        )

        Text(
            goal.name.uppercase(Locale.getDefault()),
            style =
                textStyle.copy(
                    shadow =
                        Shadow(
                            color = MaterialTheme.colorScheme.onBackground,
                            offset = Offset(5f, 5f),
                            blurRadius = 0f,
                        ),
                ),
            textAlign = TextAlign.Center,
            maxLines = 2,
            modifier =
                Modifier.align(Alignment.Center),
        )
    }
}

@Composable
fun GameBadge(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    val tag =
        remember {
            goal.tag.findTag()
        }

    val icon =
        remember {
            TagHelper.findBadgeResource(goal.badge, tag)
        }

    val mainColor =
        remember {
            tag.colors.last()
        }
    val brush = tag.tagGradient(isAnimated)

    val shape = TriangleShape(true)

    val textStyle =
        MaterialTheme.typography.displaySmall.copy(
            color = MaterialTheme.colorScheme.background,
            fontFamily =
                FontFamily(
                    Font(R.font.corporate_games, FontWeight.Normal),
                ),
        )

    Box(modifier) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .offset(2.dp, 4.dp),
        )

        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(.7f)
                .background(brush, shape)
                .border(2.dp, MaterialTheme.colorScheme.onBackground, shape)
                .align(Alignment.BottomCenter),
        )

        Image(
            painter = painterResource(id = icon),
            contentDescription = goal.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(16.dp).fillMaxSize().align(Alignment.Center),
        )

        Text(
            goal.name.uppercase(Locale.getDefault()),
            style = textStyle,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            textAlign = TextAlign.Start,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
        Text(
            goal.name.uppercase(Locale.getDefault()),
            style =
                textStyle.copy(
                    shadow =
                        Shadow(
                            color = tag.colors.first(),
                            offset = Offset(2f, 5f),
                            blurRadius = 0f,
                        ),
                ),
            maxLines = 1,
            textAlign = TextAlign.Start,
            modifier = Modifier.align(Alignment.BottomCenter).offset(x = (-2).dp),
        )
    }
}

@Composable
fun DefaultBadge(
    goal: Goal,
    showText: Boolean,
    isAnimated: Boolean,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val tag =
        remember {
            goal.tag.findTag()
        }
    val shape =
        remember {
            Flow(context)
        }

    val icon =
        remember {
            TagHelper.findBadgeResource(goal.badge, tag)
        }

    val brush = tag.tagGradient(isAnimated)

    val textColor =
        MaterialTheme.colorScheme.onBackground.copy(
            alpha = .7f,
        )
    val textStyle =
        MaterialTheme.typography.titleMedium.copy(
            color = tag.textColor,
            fontWeight = FontWeight.Bold,
            letterSpacing = 5.sp,
        )

    val smallTextStyle =
        MaterialTheme.typography.bodySmall.copy(
            color = tag.textColor.copy(alpha = .4f),
            letterSpacing = 10.5.sp,
        )

    Box(
        modifier
            .border(2.dp, MaterialTheme.colorScheme.onBackground, shape)
            .background(brush, shape),
    ) {
        Image(
            Icons.Rounded.Star,
            contentDescription = goal.name,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(tag.textColor),
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(
                        tag.colors.first(),
                        CircleShape,
                    ).border(1.dp, brush, CircleShape),
        )
    }
}
