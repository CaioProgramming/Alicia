package com.ilustris.alicia.features.messages.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ilustris.alicia.R
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

        item {
            Box(Modifier.fillMaxWidth()) {
                Image(
                    painterResource(id = R.drawable.pretty_girl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .size(100.dp)
                            .clip(CircleShape),
                )
            }
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }
}
