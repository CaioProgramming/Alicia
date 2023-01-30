package com.ilustris.alicia.features.messages.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.domain.model.MessageInfo
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.toolbarColor
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import java.util.Calendar

@Composable
fun MessageBubble(
    messageData: MessageInfo,
    movimentations: List<Movimentation> = emptyList(),
    modifier: Modifier,
    amount: Double = 0.0
) {
    val date = Calendar.getInstance()
    val message = messageData.message

    date.timeInMillis = message.sentTime
    val isUserMessage = message.type == Type.USER
    val shape = getCardShape(isUserMessage)
    val color =
        if (isUserMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
    val textColor =
        if (isUserMessage) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary
    val horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start
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
                .wrapContentHeight()
        ) {
            Card(
                shape = shape,
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = color)
            ) {
                Text(
                    text = message.message,
                    style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                    modifier = Modifier.padding(16.dp)
                )
                if (messageData.observeMovimentations) {
                    when(message.type) {
                        Type.AMOUNT ->AmountComponent(amount = amount)
                        Type.PROFIT_HISTORY,
                        Type.LOSS_HISTORY ->  Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .background(
                                    color = toolbarColor(isSystemInDarkTheme()),
                                    shape = shape
                                )
                        ) {
                            movimentations.forEach {
                                StatementComponent(description = it.description, value = it.value)
                            }
                        }
                    }
                }
            }
        }
        Text(
            text = date.time.format(DateFormats.HH_MM),
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.4f
                )
            ), modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}


fun getCardShape(isUserMessage: Boolean) = if (isUserMessage) RoundedCornerShape(
    15.dp,
    15.dp,
    0.dp,
    15.dp
) else RoundedCornerShape(15.dp, 15.dp, 15.dp, 0.dp)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AliciaTheme {
        Column {
            MessageBubble(
                modifier = Modifier.fillMaxWidth(),
                messageData = MessageInfo(
                    Message(
                        "25 de Janeiro",
                        type = Type.HEADER,
                        sentTime = 1674477903527
                    )
                )
            )

            MessageBubble(
                modifier = Modifier.fillMaxWidth(),
                messageData = MessageInfo(
                    Message(
                        "Oi Alicia",
                        type = Type.USER,
                        sentTime = 1674477903527
                    )
                )
            )
            MessageBubble(
                modifier = Modifier.fillMaxWidth(),
                messageData = MessageInfo(
                    Message(
                        "Oi eu sou a Alicia",
                        type = Type.NONE,
                        sentTime = 1674477903527
                    )
                )
            )

            MessageBubble(
                modifier = Modifier.fillMaxWidth(),
                messageData = MessageInfo(
                    Message(
                        "Como posso chamar você?",
                        type = Type.NAME,
                        sentTime = 1674477903527
                    )
                )
            )
            MessageBubble(
                modifier = Modifier.fillMaxWidth(),
                messageData = MessageInfo(
                    Message(
                        "Fala ai, quanto vc ganhou hoje?",
                        type = Type.GAIN,
                        sentTime = 1674477903527
                    )
                )
            )
            MessageBubble(
                messageData = MessageInfo(
                    Message("Seu saldo é de ", type = Type.AMOUNT),
                    observeMovimentations = true
                ),
                modifier = Modifier.fillMaxWidth()
            )
            MessageBubble(
                amount = 400.0,
                modifier = Modifier.fillMaxWidth(),
                messageData = MessageInfo(
                    Message("Aqui estão seus gastos!", type = Type.PROFIT_HISTORY),
                    listOf(
                        Movimentation(description = "Nike Air", value = 500.00)
                    ),
                    true,
                ),
            )
        }
    }
}