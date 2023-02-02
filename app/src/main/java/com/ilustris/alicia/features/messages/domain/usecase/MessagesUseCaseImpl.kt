package com.ilustris.alicia.features.messages.domain.usecase

import android.icu.util.Calendar
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.domain.mapper.MessageMapper
import com.ilustris.alicia.features.messages.domain.model.MessageInfo
import com.ilustris.alicia.features.messages.domain.repository.MessageRepository
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessagesUseCaseImpl @Inject constructor(
    private val repository: MessageRepository,
    private val messageMapper: MessageMapper
) :
    MessagesUseCase {

    override suspend fun saveMessage(message: Message) {
        val calendar = Calendar.getInstance()
        val newMessage = message.copy(sentTime = calendar.timeInMillis)
        repository.saveMessage(newMessage)
    }

    override fun getMessages(): Flow<List<MessageInfo>> = flow {
        repository.getMessages().collect {
            val mappedMessages = mapMessages(it)
            emit(mappedMessages)
        }
    }

    override suspend fun getLastMessage(): MessageInfo? {
        repository.getLastMessage()?.let {
            return messageMapper.mapMessageToInfo(it)
        }
        return null
    }


    private fun mapMessages(messages: List<Message>): ArrayList<MessageInfo> {
        val messagesGrouped = ArrayList<MessageInfo>()
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
            val messagesInfo = messages.map { messageMapper.mapMessageToInfo(it) }
            messagesGrouped.addAll(messagesInfo)
            messagesGrouped.add(messagesGrouped.size, messageMapper.mapMessageToInfo(dayMessage))
        }
        return messagesGrouped
    }

}