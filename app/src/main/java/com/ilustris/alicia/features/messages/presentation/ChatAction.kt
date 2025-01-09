package com.ilustris.alicia.features.messages.presentation

sealed class ChatAction {
    object FetchUser : ChatAction()

    data class SendMessage(
        val message: String,
    ) : ChatAction()
}
