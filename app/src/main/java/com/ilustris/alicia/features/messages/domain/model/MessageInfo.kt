package com.ilustris.alicia.features.messages.domain.model

import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Type

data class MessageInfo(
    val message: Message,
    val attachedSuggestions: List<Suggestion> = emptyList(),
)

data class MessageGroup(
    val title: String,
    val messages: List<Message>,
)
