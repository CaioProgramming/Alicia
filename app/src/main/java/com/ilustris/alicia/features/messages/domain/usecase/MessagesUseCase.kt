package com.ilustris.alicia.features.messages.domain.usecase

import com.ilustris.alicia.features.messages.data.model.Message
import kotlinx.coroutines.flow.Flow

interface MessagesUseCase {

   suspend fun saveMessage(message: Message)

   fun getMessages() : Flow<List<Message>>

}