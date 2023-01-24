package com.ilustris.alicia.features.messages.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.messages.data.model.Action
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.toolbarColor
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MessageBubble(messageData: Message, onMessageClick: (Message) -> Unit) {
    val date = Calendar.getInstance()
    var visible = remember { MutableTransitionState(false).apply { targetState = true } }
    date.timeInMillis = messageData.sentTime
    val isUserMessage = messageData.action == Action.USER
    val shape = getCardShape(isUserMessage)
    val color =
        if (isUserMessage) MaterialTheme.colorScheme.primary else toolbarColor(isSystemInDarkTheme())
    val horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start
    AnimatedVisibility(
        visibleState = visible,
        enter = scaleIn() + expandHorizontally(expandFrom = horizontalAlignment),
        exit = scaleOut() + shrinkHorizontally(shrinkTowards = horizontalAlignment)
    ) {
        Column(horizontalAlignment = horizontalAlignment, modifier = Modifier.fillMaxWidth()) {
            Card(
                shape = shape,
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .animateEnterExit(
                        enter = slideInHorizontally(),
                        exit = slideOutHorizontally()
                    ),
                colors = CardDefaults.cardColors(containerColor = color)
            ) {
                Text(
                    text = messageData.message,
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.padding(16.dp)

                )
            }
            Text(
                text = date.time.format(DateFormats.HH_MM),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(
                        alpha = 0.4f
                    )
                ), modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
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
                messageData = Message(
                    "Oi Alicia",
                    action = Action.USER,
                    sentTime = 1674477903527
                ), {})
            MessageBubble(
                messageData = Message(
                    "Oi eu sou a Alicia",
                    action = Action.NONE,
                    sentTime = 1674477903527
                ), {})
            MessageBubble(
                messageData = Message(
                    "Como posso chamar você?",
                    action = Action.NAME,
                    sentTime = 1674477903527
                ), {})
            MessageBubble(
                messageData = Message(
                    "Fala ai, quanto vc ganhou hoje?",
                    action = Action.GAIN,
                    sentTime = 1674477903527
                ), {})
        }
    }
}