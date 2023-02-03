package com.ilustris.alicia.features.messages.domain.model

import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Type

data class MessageInfo(
    val message: Message,
    val observeMovimentations: Boolean = (message.type == Type.PROFIT_HISTORY || message.type == Type.LOSS_HISTORY || message.type == Type.AMOUNT),
    val attachedSuggestions: List<Suggestion> = emptyList()
)
