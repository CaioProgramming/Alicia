package com.ilustris.alicia.features.messages.domain.usecase

import com.ilustris.alicia.ai.model.ai.AIResponse
import com.ilustris.alicia.ai.usecase.RequestResult
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.domain.model.MessageGroup
import kotlinx.coroutines.flow.Flow

interface ChatUseCase {
    suspend fun saveMessage(
        message: Message,
        extraKey: String? = null,
    )

    suspend fun getMessages(): Flow<List<MessageGroup>>

    suspend fun getLastMessage(): Message?

    suspend fun saveGeneratedMessage(
        aiResponse: AIResponse,
        extraKey: String?,
    ): RequestResult<Exception, Message>
}
