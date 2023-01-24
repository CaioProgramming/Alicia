package com.ilustris.alicia.features.messages.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.ui.components.MessageBubble

@Composable
fun MessagesList(modifier: Modifier, appMessages: State<List<Message>>, onSelectMessage: (Message) -> Unit) {
    val messages = remember { appMessages }
    LazyColumn(
        reverseLayout = true,
        modifier = modifier
    ) {
        itemsIndexed(messages.value) { index, message ->
            MessageBubble(message, onSelectMessage)
        }
    }
}

