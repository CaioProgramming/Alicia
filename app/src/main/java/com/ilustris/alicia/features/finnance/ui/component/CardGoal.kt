package com.ilustris.alicia.features.finnance.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.ui.theme.toolbarColor
import com.ilustris.alicia.utils.formatToCurrencyText
import kotlin.random.Random

@Composable
fun CardGoal(goal: Goal, amount: Double) {
    val progression = ((amount / goal.value)).toFloat()

    var showedSnack by remember {
        mutableStateOf(false)
    }
    var progress by remember { mutableStateOf(0f) }
    val progressAnimDuration = 1500
    val progressAnimation by animateFloatAsState(
        targetValue = progression,
        animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing)
    )
    LaunchedEffect(progression) {
        progress = progression
    }

    val cardColor = if (progression > 1f) MaterialTheme.colorScheme.primary else toolbarColor(
        isSystemInDarkTheme()
    )
    val textColor =
        if (progression > 1f) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()



    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier.padding(4.dp)
    ) {

        ConstraintLayout(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            val (emoji, progress, currentValue, description) = createRefs()
            Text(
                text = goal.tag.emoji,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(color = textColor),
                modifier = Modifier
                    .constrainAs(emoji) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding(16.dp)
            )
            Text(
                text = goal.description,
                modifier = Modifier
                    .constrainAs(description) {
                        top.linkTo(progress.top)
                        bottom.linkTo(progress.bottom)
                        start.linkTo(progress.end)
                        end.linkTo(currentValue.start)
                        width = Dimension.fillToConstraints
                    }
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W700,
                    color = textColor
                ),
                textAlign = TextAlign.Start
            )
            Text(
                text = goal.value.formatToCurrencyText(),
                style = MaterialTheme.typography.labelSmall.copy(color = textColor),
                modifier = Modifier
                    .constrainAs(currentValue) {
                        top.linkTo(progress.top)
                        bottom.linkTo(progress.bottom)
                        end.linkTo(parent.end)
                    }
                    .padding(1.dp)
            )
            CircularProgressIndicator(
                progress = progressAnimation,
                modifier = Modifier.constrainAs(progress) {
                    top.linkTo(emoji.top)
                    bottom.linkTo(emoji.bottom)
                    start.linkTo(emoji.start)
                    end.linkTo(emoji.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.01f)
            )
        }
    }
}

@Preview
@Composable
fun goalPreview() {
    CardGoal(
        amount = 50.00,
        goal = Goal(
            value = 500.00,
            description = "Casa",
            tag = Tag.GIFTS,
            createdAt = Random.nextLong()
        )
    )
}