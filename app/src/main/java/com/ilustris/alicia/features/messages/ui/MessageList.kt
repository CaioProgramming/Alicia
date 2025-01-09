package com.ilustris.alicia.features.messages.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.core.theme.themeBrush
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.domain.model.MessageGroup
import com.ilustris.alicia.features.messages.ui.components.MessageBubble

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessagesList(
    messages: List<MessageGroup>,
    modifier: Modifier,
    listState: LazyListState,
    brush: Brush = themeBrush(),
    onOpenMessage: (Message) -> Unit,
) {
    LazyColumn(
        reverseLayout = true,
        modifier = modifier,
        state = listState,
    ) {
        messages.forEach {
            items(it.messages, key = { m -> m.id }) {
                MessageBubble(
                    it,
                    brushBackground = brush,
                    modifier = Modifier.animateItemPlacement(),
                    openMessage = onOpenMessage,
                )
            }

            item {
                Text(
                    text = it.title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.overline,
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                )
            }
        }

        item(key = "collapse_toolbar") {
            CollapseToolbar(Modifier.padding(8.dp).fillMaxWidth().wrapContentHeight())
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }
}
