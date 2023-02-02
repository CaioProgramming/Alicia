package com.ilustris.alicia.features.messages.data.repository

import com.ilustris.alicia.features.messages.data.datasource.MessageDao
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(private val messageDao: MessageDao) : MessageRepository {

    override suspend fun saveMessage(message: Message): Long = messageDao.saveMessage(message)

    override fun getMessages(): Flow<List<Message>> = messageDao.getMessages()
    override suspend fun getLastMessage(): Message? = messageDao.getLastMessage().lastOrNull()

}