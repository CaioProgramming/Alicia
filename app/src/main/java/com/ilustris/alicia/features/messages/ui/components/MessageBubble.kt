package com.ilustris.alicia.features.messages.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.ui.theme.AliciaTheme
import com.ilustris.alicia.ui.theme.toolbarColor
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MessageBubble(messageData: Message, modifier: Modifier) {
    val date = Calendar.getInstance()
    var visible = remember {
        MutableTransitionState(false).apply {
            if (!this.currentState) {
                targetState = true
            }
        }
    }
    date.timeInMillis = messageData.sentTime
    val isUserMessage = messageData.type == Type.USER
    val shape = getCardShape(isUserMessage)
    val color =
        if (isUserMessage) MaterialTheme.colorScheme.primary else toolbarColor(isSystemInDarkTheme())
    val textColor =
        if (isUserMessage) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
    val horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start
    if(messageData.type == Type.HEADER) {
        Text(
            text = messageData.message,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W400),
        )
    } else {
        Column(horizontalAlignment = horizontalAlignment, modifier = Modifier.fillMaxWidth()) {
            Card(
                shape = shape,
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = color)
            ) {
                Text(
                    text = messageData.message,
                    style = MaterialTheme.typography.bodyLarge.copy(color = textColor),
                    modifier = Modifier.padding(16.dp)
                )
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
                messageData = Message(
                    "25 de Janeiro",
                    type = Type.HEADER,
                    sentTime = 1674477903527
                )
            )

            MessageBubble(
                modifier = Modifier.fillMaxWidth(),
                messageData = Message(
                    "Oi Alicia",
                    type = Type.USER,
                    sentTime = 1674477903527
                )
            )
            MessageBubble(
                modifier = Modifier.fillMaxWidth(),
                messageData = Message(
                    "Oi eu sou a Alicia",
                    type = Type.NONE,
                    sentTime = 1674477903527
                )
            )
            MessageBubble(
                modifier = Modifier.fillMaxWidth(),
                messageData = Message(
                    "Como posso chamar você?",
                    type = Type.NAME,
                    sentTime = 1674477903527
                )
            )
            MessageBubble(
                modifier = Modifier.fillMaxWidth(),
                messageData = Message(
                    "Fala ai, quanto vc ganhou hoje?",
                    type = Type.GAIN,
                    sentTime = 1674477903527
                )
            )
        }
    }
}