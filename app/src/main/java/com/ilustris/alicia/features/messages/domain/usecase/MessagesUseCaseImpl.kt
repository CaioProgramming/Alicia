package com.ilustris.alicia.features.messages.domain.usecase

import android.icu.util.Calendar
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.domain.repository.MessageRepository
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessagesUseCaseImpl  @Inject constructor(private val repository: MessageRepository): MessagesUseCase {

    override suspend fun saveMessage(message: Message)  {
        val calendar = Calendar.getInstance()
        val newMessage = message.copy(sentTime = calendar.timeInMillis)
        repository.saveMessage(newMessage)
    }

    override fun getMessages(): Flow<List<Message>> = repository.getMessages()


    private fun mapMessages(messages: List<Message>): ArrayList<Message> {
        val messagesGrouped = ArrayList<Message>()
        val groupedByDay = messages.groupBy {
            val calendar = java.util.Calendar.getInstance().apply {
                timeInMillis = it.sentTime
            }
            calendar[java.util.Calendar.DAY_OF_YEAR]
        }
        groupedByDay.forEach { (day, messages) ->
            val firstMessage = messages.first()
            val calendar = java.util.Calendar.getInstance().apply {
                timeInMillis = firstMessage.sentTime
            }
            val dayMessage = firstMessage.copy(
                id = firstMessage.sentTime.toInt(),
                message = calendar.time.format(DateFormats.DD_OF_MM),
                type = Type.HEADER
            )
            messages.forEach { message ->
                messagesGrouped.add(message)
            }
            messagesGrouped.add(0, dayMessage)
        }
        return messagesGrouped
    }

}