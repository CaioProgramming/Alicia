package com.ilustris.alicia.features.messages.domain.usecase

import android.icu.util.Calendar
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessagesUseCaseImpl  @Inject constructor(private val repository: MessageRepository): MessagesUseCase {

    override suspend fun saveMessage(message: Message)  {
        val calendar = Calendar.getInstance()
        val newMessage = message.copy(sentTime = calendar.timeInMillis)
        repository.saveMessage(newMessage)
    }

    override fun getMessages(): Flow<List<Message>> = repository.getMessages()

}