package com.ilustris.alicia.features.messages.domain.usecase

import android.icu.util.Calendar
import android.util.Log
import com.ilustris.alicia.ai.model.ai.AIResponse
import com.ilustris.alicia.ai.usecase.RequestResult
import com.ilustris.alicia.features.finnance.domain.usecase.FinanceUseCase
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Sender
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.domain.model.MessageGroup
import com.ilustris.alicia.features.messages.domain.repository.MessageRepository
import com.ilustris.alicia.utils.DateFormats
import com.ilustris.alicia.utils.format
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatUseCaseImpl
    @Inject
    constructor(
        private val repository: MessageRepository,
        private val financeUseCase: FinanceUseCase,
    ) : ChatUseCase {
        override suspend fun saveMessage(
            message: Message,
            extraKey: String?,
        ) {
            val calendar = Calendar.getInstance()
            val newMessage = message.copy(sentTime = calendar.timeInMillis, extraDataKey = extraKey)

            Log.i(javaClass.simpleName, "saveMessage: $newMessage")
            repository.saveMessage(newMessage)
        }

        override suspend fun getMessages(): Flow<List<MessageGroup>> =
            repository.getMessages().map {
                mapMessages(it)
            }

        override suspend fun getLastMessage() = repository.getLastMessage()

        override suspend fun saveGeneratedMessage(
            aiResponse: AIResponse,
            extraKey: String?,
        ): RequestResult<Exception, Message> {
            try {
                Log.d(javaClass.simpleName, "saveGeneratedMessage: Trying to save $aiResponse")
                val message =
                    aiResponse.mapToMessage()
                        ?: return RequestResult.Error(Exception("Error mapping message"))

                saveMessage(message, extraKey)
                return RequestResult.Success(message)
            } catch (e: Exception) {
                e.printStackTrace()
                return RequestResult.Error(e)
            }
        }

        private suspend fun getExtraData(message: Message): Any? {
            try {
                val key = message.extraDataKey
                when (message.findType()) {
                    Type.MOVIMENTATION -> {
                        if (message.extraDataKey == null) return null
                        val movimentation =
                            financeUseCase
                                .getMovimentationByIdSync(message.extraDataKey.toLong())
                        return movimentation
                    }
                    Type.HISTORY -> {
                        return financeUseCase.getAllMovimentationsSync()
                    }
                    Type.GOAL -> {
                        if (key == null) return null
                        return financeUseCase.getGoalByIdSync(key.toLong())
                    }
                    Type.BALANCE -> {
                        return financeUseCase.getAmountSync()
                    }
                    else -> return null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        private suspend fun mapMessages(messages: List<Message>): List<MessageGroup> {
            // Log.i(javaClass.simpleName, "mapMessages: Mapping messages $messages")
            val groupedByDay =
                messages.groupBy {
                    val calendar =
                        java.util.Calendar.getInstance().apply {
                            timeInMillis = it.sentTime
                        }
                    calendar[java.util.Calendar.DAY_OF_YEAR]
                }
            // Log.i(javaClass.simpleName, "mapMessages: ${groupedByDay.size} groups found")
            return groupedByDay.map {
                // Log.i(javaClass.simpleName, "mapMessages: Mapping group ${it.key} with ${it.value.size} messages")
                val firstMessage = messages.first()
                val calendar =
                    java.util.Calendar.getInstance().apply {
                        timeInMillis = firstMessage.sentTime
                    }
                messages.forEach { message ->
                    val attachment = getExtraData(message)

                    attachment?.let {
                        // Log.d(javaClass.simpleName, "mapMessages: Extra data founded $attachment for $message")
                        message.extraData = attachment
                        Log.d(javaClass.simpleName, "mapMessages: attachment\n$it\n added to message $message")
                    }
                }
                val dataGroup =
                    MessageGroup(
                        title = calendar.time.format(DateFormats.DD_OF_MM),
                        messages = messages,
                    )
                // Log.i(javaClass.simpleName, "mapMessages: Group mapped $dataGroup")
                dataGroup
            }
        }
    }

fun AIResponse.mapToMessage(extraData: String? = null): Message? =
    Message(
        text = this.text,
        sender = Sender.BOT,
        type = getMessageType()?.name,
        extraData = extraData ?: "",
    )

fun AIResponse.getMessageType(): Type? = Type.entries.find { it.name.lowercase() == this.type?.lowercase() }
