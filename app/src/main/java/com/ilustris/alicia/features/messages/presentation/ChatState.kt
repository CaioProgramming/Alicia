package com.ilustris.alicia.features.messages.presentation

sealed class ChatState {
    data object Idle : ChatState()

    data object Loading : ChatState()

    data object UserRequired : ChatState()

    data class Error(
        val message: String?,
    ) : ChatState()
}
