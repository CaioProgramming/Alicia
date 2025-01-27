package com.ilustris.alicia.features.messages.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.R
import com.ilustris.alicia.core.theme.AliciaTheme
import com.ilustris.alicia.core.theme.themeBrush
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo
import com.ilustris.alicia.features.finnance.ui.component.AmountComponent
import com.ilustris.alicia.features.finnance.ui.component.CardStatement
import com.ilustris.alicia.features.finnance.ui.component.GoalMedal
import com.ilustris.alicia.features.finnance.ui.component.GoalMedalV3
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Sender
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageBubble(
    message: Message,
    modifier: Modifier,
    brushBackground: Brush = themeBrush(),
    openMessage: (Message) -> Unit,
) {
    @Composable
    fun viewForExtraData(
        type: Type?,
        data: Any?,
    ) {
        data?.let {
            when (type) {
                Type.MOVIMENTATION ->
                    StatementCard(
                        movimentation = data as Movimentation,
                        showDivider = false,
                        modifier =
                            Modifier
                                .padding(8.dp)
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = .4f),
                                    RoundedCornerShape(10.dp),
                                ),
                    )
                Type.GOAL ->
                    GoalMedalV3(
                        goal = data as Goal,
                        showText = true,
                        isAnimated = true,
                        Modifier.size(200.dp),
                    )
                Type.BALANCE ->
                    AmountComponent(
                        amount = data as Double,
                    )
                Type.HISTORY ->
                    MovimentationHorizontalList(
                        movimentations = (data as List<MovimentationInfo>),
                        modifier = modifier,
                    )
                null -> Text("No extra data.")
            }
        }
    }

    val date = Calendar.getInstance()
    date.timeInMillis = message.sentTime
    val isUserMessage = message.sender == Sender.USER
    val shape = getCardShape(isUserMessage)
    val color =
        if (isUserMessage) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
    val textColor = MaterialTheme.colorScheme.onPrimaryContainer
    val horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start
    val showDate = remember { mutableStateOf(false) }
    val extraDataObject =
        remember {
            message.extraData?.let {
                try {
                    when (message.findType()) {
                        Type.MOVIMENTATION -> it as Movimentation
                        Type.GOAL -> it as Goal
                        Type.BALANCE -> it as Double
                        Type.HISTORY -> it as List<MovimentationInfo>
                        else -> null
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
    Column(
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
    ) {
        Text(
            text = message.text,
            style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
            modifier =
                Modifier
                    .padding(4.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onBackground.copy(alpha = .1f),
                        shape,
                    ).background(
                        color = color,
                        shape = shape,
                    ).padding(16.dp)
                    .clickable {
                        showDate.value = !showDate.value
                        openMessage(message)
                    },
        )

        AnimatedVisibility(visible = showDate.value) {
            Text(
                text = date.time.format(DateFormats.HH_MM),
                style =
                    MaterialTheme.typography.labelSmall.copy(
                        color =
                            MaterialTheme.colorScheme.onBackground.copy(
                                alpha = 0.4f,
                            ),
                    ),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        extraDataObject?.let {
            viewForExtraData(message.findType(), it)
        }
    }
}

@Composable
private fun MovimentationHorizontalList(
    movimentations: List<MovimentationInfo>,
    modifier: Modifier,
) {
    val movimentationGroups = movimentations
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        items(movimentationGroups) {
            CardStatement(
                tag = it.tag,
                movimentations = it.movimentations,
                {},
            )
        }
    }
}

@Composable
private fun GoalHorizontalList(
    modifier: Modifier,
    goals: List<Goal>,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(goals) {
            GoalMedal(goal = it, preffSize = 50.dp, enabled = false)
        }
    }
}

private val bubbleRadius = 25.dp

fun getCardShape(isUserMessage: Boolean) =
    if (isUserMessage) {
        RoundedCornerShape(bubbleRadius).copy(bottomEnd = CornerSize(0.dp))
    } else {
        RoundedCornerShape(
            bubbleRadius,
        ).copy(bottomStart = CornerSize(0.dp))
    }

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AliciaTheme {
        LazyColumn {
            Message(
                94151,
                type = null,
                sender = Sender.USER,
                sentTime = 1674477903527,
                text = "Hello",
            )
            items(Type.values()) {
                val extraData: Any? =
                    when (it) {
                        Type.MOVIMENTATION ->
                            Movimentation(
                                0,
                                0.0,
                                "Test",
                                Tag.ENTERTAINMENT.name,
                                1674477903527,
                            )

                        Type.GOAL ->
                            Goal(
                                0,
                                100.00,
                                "test",
                                1674477903527,
                                Tag.EDUCATION.name,
                                badge = R.drawable.car_badge_1,
                            )
                        Type.BALANCE ->
                            5000.00

                        Type.HISTORY ->
                            List(5) {
                                val value = if (it % 2 == 0) 5000.0 else 200.00.unaryMinus()
                                Movimentation(
                                    0,
                                    value,
                                    "Test",
                                    Tag.HEALTH.name,
                                    1674477903527,
                                )
                            }

                        else -> null
                    }

                MessageBubble(
                    message =
                        Message(
                            94151,
                            type = it.name,
                            extraData = extraData,
                            sender = Sender.BOT,
                            sentTime = 1674477903527,
                            text = "This a message!",
                        ),
                    modifier = Modifier.wrapContentSize(),
                    openMessage = { },
                )
            }

            item {
                Message(
                    94151,
                    type = null,
                    sender = Sender.USER,
                    sentTime = 1674477903527,
                    text = "Bye bye",
                )
            }
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            MessageBubble(
                message =
                    Message(
                        94151,
                        type = null,
                        sender = Sender.USER,
                        sentTime = 1674477903527,
                        text = "Hello",
                    ),
                modifier = Modifier.wrapContentSize(),
                openMessage = { },
            )

            MessageBubble(
                message =
                    Message(
                        94151,
                        sender = Sender.BOT,
                        sentTime = 1674477903527,
                        text = "Hello Human",
                    ),
                modifier = Modifier.wrapContentSize(),
                openMessage = { },
            )
        }
    }
}
