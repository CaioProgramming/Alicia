package com.ilustris.alicia.features.messages.domain.repository

import com.ilustris.alicia.features.messages.data.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun saveMessage(message: Message): Long

    fun getMessages(): Flow<List<Message>>

    suspend fun getLastMessage(): Message?
}