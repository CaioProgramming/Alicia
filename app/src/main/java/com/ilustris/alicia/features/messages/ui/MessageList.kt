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
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo

import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.domain.model.MessageInfo
import com.ilustris.alicia.features.messages.domain.model.Suggestion
import com.ilustris.alicia.features.messages.ui.components.MessageBubble

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessagesList(
    modifier: Modifier,
    appMessages: State<List<MessageInfo>>,
    profits: List<MovimentationInfo>,
    losses: List<MovimentationInfo>,
    goals: List<Goal>,
    amount: Double,
    onSelectSuggestion: (Suggestion, String?) -> Unit

) {
    val messages = remember { appMessages }
    val scrollState = rememberLazyListState()

    LazyColumn(
        reverseLayout = true,
        modifier = modifier,
        state = scrollState
    ) {
        itemsIndexed(messages.value, key = { index, item -> item.message.id }) { index, message ->
            val movementList =
                if (message.message.type == Type.PROFIT_HISTORY) profits else if (message.message.type == Type.LOSS_HISTORY) losses else emptyList()
            MessageBubble(
                message,
                movementList,
                goals,
                modifier = Modifier.animateItemPlacement(),
                amount,
                onSelectSuggestion
            )
        }
    }

    LaunchedEffect(messages.value.size) {
        if (messages.value.isNotEmpty()) {
            scrollState.animateScrollToItem(0)
        }
    }

}

