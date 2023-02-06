package com.ilustris.alicia.features.messages.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo
import com.ilustris.alicia.features.finnance.ui.CardGoal
import com.ilustris.alicia.features.finnance.ui.CardStatement
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.domain.model.MessageInfo
import com.ilustris.alicia.features.messages.domain.model.Suggestion
import com.ilustris.alicia.features.messages.ui.MessageSuggestionsList
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageBubble(
    messageData: MessageInfo,
    movimentations: List<MovimentationInfo> = emptyList(),
    goals: List<Goal> = emptyList(),
    modifier: Modifier,
    amount: Double = 0.0,
    onSelectSuggestion: (Suggestion, String?) -> Unit
) {

    val date = Calendar.getInstance()
    val message = messageData.message
    date.timeInMillis = message.sentTime
    val isUserMessage = message.type == Type.USER
    val shape = getCardShape(isUserMessage)
    val color =
        if (isUserMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
    val textColor =
        if (isUserMessage) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
    val horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start
    val showDate = remember { mutableStateOf(false) }


    if (message.type == Type.HEADER) {
        Text(
            text = message.message,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W300),
        )
    } else {
        Column(
            horizontalAlignment = horizontalAlignment, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Card(
                shape = shape,
                onClick = {
                    showDate.value = !showDate.value
                },
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .wrapContentSize(),
                colors = CardDefaults.cardColors(containerColor = color)
            ) {
                Text(
                    text = message.message,
                    style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                    modifier = Modifier.padding(16.dp)
                )
            }
            AnimatedVisibility(visible = showDate.value) {
                Text(
                    text = date.time.format(DateFormats.HH_MM),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.4f
                        )
                    ), modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            if (messageData.attachedSuggestions.isNotEmpty()) {
                MessageSuggestionsList(
                    modifier = Modifier.wrapContentSize(),
                    suggestions = messageData.attachedSuggestions,
                    onSelectSuggestion = onSelectSuggestion
                )
            }
            if (messageData.observeMovimentations) {
                when (message.type) {
                    Type.AMOUNT -> AmountComponent(amount = amount)
                    Type.GOAL -> Column {
                        goals.forEach {
                            CardGoal(goal = it, amount = amount)
                        }
                    }
                    Type.PROFIT_HISTORY,
                    Type.LOSS_HISTORY -> LazyRow(
                        modifier = modifier,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        items(movimentations) {
                            CardStatement(
                                tag = it.tag,
                                movimentations = it.movimentations
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}

private val bubbleRadius = 25.dp

fun getCardShape(isUserMessage: Boolean) =
    if (isUserMessage) RoundedCornerShape(bubbleRadius).copy(bottomEnd = CornerSize(0.dp)) else RoundedCornerShape(
        bubbleRadius
    ).copy(bottomStart = CornerSize(0.dp))

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AliciaTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            MessageBubble(
                modifier = Modifier.wrapContentSize(),
                messageData = MessageInfo(
                    Message(
                        "25 de Janeiro",
                        type = Type.HEADER,
                        sentTime = 1674477903527
                    )
                ),
                onSelectSuggestion = { suggestion, s -> }
            )

            MessageBubble(
                modifier = Modifier.wrapContentSize(),
                messageData = MessageInfo(
                    Message(
                        "Oi Alicia, tudo bem? Estou buscando amizades novas, por favor fale comigo, eu preciso saber se alguma coisa sente algo por mim qualquer coisa q seja",
                        type = Type.USER,
                        sentTime = 1674477903527
                    )
                ),
                onSelectSuggestion = { suggestion, s -> }
            )
            MessageBubble(
                modifier = Modifier.wrapContentSize(),
                messageData = MessageInfo(
                    Message(
                        "Oi eu sou a Alicia",
                        type = Type.NONE,
                        sentTime = 1674477903527
                    )
                ),
                onSelectSuggestion = { suggestion, s -> }
            )

            MessageBubble(
                modifier = Modifier.wrapContentSize(),
                messageData = MessageInfo(
                    Message(
                        "Como posso chamar você?",
                        type = Type.NAME,
                        sentTime = 1674477903527
                    )
                ),
                onSelectSuggestion = { suggestion, s -> }
            )
            MessageBubble(
                modifier = Modifier.wrapContentSize(),
                messageData = MessageInfo(
                    Message(
                        "Fala ai, quanto vc ganhou hoje?",
                        type = Type.PROFIT,
                        sentTime = 1674477903527
                    )
                ),
                onSelectSuggestion = { suggestion, s -> }
            )
            MessageBubble(
                messageData = MessageInfo(
                    Message("Seu saldo é de ", type = Type.AMOUNT),
                    observeMovimentations = true
                ),
                modifier = Modifier.wrapContentSize(),
                onSelectSuggestion = { suggestion, s -> }
            )
            MessageBubble(
                amount = 400.0,
                modifier = Modifier.wrapContentSize(),
                messageData = MessageInfo(
                    Message("Aqui estão seus gastos!", type = Type.PROFIT_HISTORY),
                ),
                onSelectSuggestion = { suggestion, s -> }
            )
        }
    }
}