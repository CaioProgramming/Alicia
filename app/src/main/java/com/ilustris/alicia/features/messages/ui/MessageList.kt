package com.ilustris.alicia.features.messages.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.ui.components.MessageBubble

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessagesList(modifier: Modifier, appMessages: State<List<Message>>) {
    val messages = remember { appMessages }
    val scrollState = rememberLazyListState()

    LazyColumn(
        reverseLayout = true,
        modifier = modifier,
        state = scrollState
    ) {
        itemsIndexed(messages.value, key = { index, item -> item.id }) { index, message ->
            MessageBubble(message, modifier = Modifier.animateItemPlacement())
        }
    }

    LaunchedEffect(messages.value.size) {
        if (messages.value.isNotEmpty()) {
            scrollState.animateScrollToItem(0)
        }
    }

}

