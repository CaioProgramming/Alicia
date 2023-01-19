package com.ilustris.alicia.features.messages.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ilustris.alicia.features.home.ui.MockData
import com.ilustris.alicia.features.messages.domain.model.Message
import com.ilustris.alicia.features.messages.ui.components.MessageBubble

@Composable
fun MessagesList(modifier: Modifier, onSelectMessage: (Message) -> Unit) {
    val messages = remember { MockData.messages }
    LazyColumn(
        reverseLayout = true,
        modifier = modifier
    ) {
        items(messages) {
            MessageBubble(messageData = it, onSelectMessage)
        }
    }
}