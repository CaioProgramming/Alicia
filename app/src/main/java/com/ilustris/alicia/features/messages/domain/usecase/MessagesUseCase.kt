package com.ilustris.alicia.features.messages.domain.usecase

import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.domain.model.MessageInfo
import kotlinx.coroutines.flow.Flow

interface MessagesUseCase {

   suspend fun saveMessage(message: Message)

   fun getMessages() : Flow<List<MessageInfo>>

}