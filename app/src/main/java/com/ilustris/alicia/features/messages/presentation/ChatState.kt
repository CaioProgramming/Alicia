package com.ilustris.alicia.features.messages.presentation

import ai.atick.material.MaterialColor
import androidx.compose.ui.graphics.Color

sealed class ChatState {
    data object Idle : ChatState()

    data object Loading : ChatState()

    data class Notification(
        val message: String?,
        val backgroundColor: Color = MaterialColor.RedA200,
    ) : ChatState()
}
