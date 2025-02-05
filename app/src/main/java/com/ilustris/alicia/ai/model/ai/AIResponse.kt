package com.ilustris.alicia.ai.model.ai

import com.ilustris.alicia.features.messages.domain.model.Action

data class AIResponse(
    val text: String,
    val type: String?,
)

data class AICallBack(
    val action: Action,
)

data class AISuggestions(
    val suggestions: List<String>,
)
