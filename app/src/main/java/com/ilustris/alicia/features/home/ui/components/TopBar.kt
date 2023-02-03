package com.ilustris.alicia.features.home.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Visibility
import com.ilustris.alicia.R
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.toolbarColor

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes icon: Int,
    onClickNavigation: () -> Unit
) {
    ConstraintLayout(
        modifier
            .padding(vertical = 4.dp)
            .background(toolbarColor(isSystemInDarkTheme()))
    ) {
        val (profile, moreButton, divider) = createRefs()
        val infiniteTransition = rememberInfiniteTransition()
        val rotationAnimation = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(tween(1500, easing = LinearEasing))
        )
        val aliciaBrush = Brush.linearGradient(
            (listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.tertiary
            ))
        )
        Column(
            modifier = Modifier.constrainAs(profile) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            Image(
                painterResource(id = icon),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .drawBehind {
                        rotate(rotationAnimation.value) {
                            drawCircle(aliciaBrush, style = Stroke(40f))
                        }
                    }
                    .padding(4.dp)
            )

            Text(
                text = title,
                style = MaterialTheme
                    .typography
                    .bodySmall
                    .copy(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.padding(vertical = 4.dp)
            )


        }

        IconButton(
            modifier = Modifier.constrainAs(moreButton) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                visibility = Visibility.Gone
            },
            onClick = {
                onClickNavigation()
            },
            colors = IconButtonDefaults
                .iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_send_24),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                contentDescription = "enviar",
            )
        }
        Box(
            modifier = Modifier
                .constrainAs(divider) {
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f))
        )

    }

}

@Preview(name = "TopBar")
@Composable
private fun PreviewTopBarr() {
    AliciaTheme {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface),
            icon = R.drawable.pretty_girl,
            title = "Alicia",
            onClickNavigation = {})
    }

}