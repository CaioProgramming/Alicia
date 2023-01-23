package com.ilustris.alicia.features.messages.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ilustris.alicia.features.messages.data.model.Message
import kotlinx.coroutines.flow.Flow


@Dao
interface MessageDao {

    @Insert
    suspend fun saveMessage(message: Message) : Long

    @Query("SELECT * FROM MESSAGE ORDER BY sentTime DESC")
    fun getMessages() : Flow<List<Message>>

}