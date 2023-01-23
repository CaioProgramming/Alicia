package com.ilustris.alicia.features.messages.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.features.home.ui.MockData
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

