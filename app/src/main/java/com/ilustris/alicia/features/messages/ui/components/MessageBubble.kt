package com.ilustris.alicia.features.messages.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.domain.model.Message
import com.ilustris.alicia.ui.theme.AliciaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageBubble(messageData: Message, onMessageClick: (Message) -> Unit) {
    val isUserMessage = messageData.action == Action.USER
    val shape = if (isUserMessage) RoundedCornerShape(15.dp, 15.dp, 0.dp, 15.dp) else RoundedCornerShape(15.dp,15.dp,15.dp,0.dp)
    val color = if (isUserMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
    val horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start
    Column(horizontalAlignment = horizontalAlignment, modifier = Modifier.fillMaxWidth()) {
        Card(
            shape = shape,
            onClick = {
                      onMessageClick(messageData)
            },
            modifier = Modifier.padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = color)
        ) {

            Text(
                text = messageData.message,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AliciaTheme {
        Column {
            MessageBubble(messageData = Message("Oi Alicia", action = Action.USER), {})
            MessageBubble(messageData = Message("Oi eu sou a Alicia", action = Action.NONE), {})
            MessageBubble(messageData = Message("Como posso chamar você?", action = Action.NAME), {})
            MessageBubble(messageData = Message("Fala ai, quanto vc ganhou hoje?", action = Action.GAIN), {})
        }
    }
}