package com.ilustris.alicia.features.messages.data.datasource

import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.utils.DatabaseBuilder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageDaoImpl @Inject constructor (private val databaseBuilder: DatabaseBuilder): MessageDao {

    private val database by lazy {  databaseBuilder.buildDataBase() }


    override suspend fun saveMessage(message: Message): Long = database.messageDao().saveMessage(message)


    override fun getMessages(): Flow<List<Message>> = database.messageDao().getMessages()
    override fun getLastMessage(): List<Message> = database.messageDao().getLastMessage()


}