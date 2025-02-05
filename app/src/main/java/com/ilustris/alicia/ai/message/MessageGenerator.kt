package com.ilustris.alicia.ai.message

import com.ilustris.alicia.ai.model.ai.AIResponse

interface MessageGenerator {
    suspend fun replyMessage(
        userName: String,
        message: String,
    ): AIResponse?

    suspend fun generateMessage(
        prompt: String,
        useTypes: Boolean,
        humorEnabled: Boolean,
    ): AIResponse?
}
