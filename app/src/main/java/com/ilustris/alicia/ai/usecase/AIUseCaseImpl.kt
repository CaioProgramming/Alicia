package com.ilustris.alicia.ai.usecase

import android.util.Log
import com.ilustris.alicia.ai.mapper.mapToMessage
import com.ilustris.alicia.ai.model.PromptConfig
import com.ilustris.alicia.ai.model.Prompts
import com.ilustris.alicia.ai.service.AIService
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Sender
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.domain.repository.MessageRepository
import kotlinx.coroutines.flow.lastOrNull
import javax.inject.Inject

class AIUseCaseImpl
    @Inject
    constructor(private val aiService: AIService, repository: MessageRepository,
    ) : AIUseCase {
        override var messageHistory = repository.getMessages()

        override suspend fun requestMessage(
            message: String,
            config: List<PromptConfig>,
        ): Message? {
            Log.i(javaClass.simpleName, "requestMessage: requesting new message")
            val messages = messageHistory.lastOrNull()?.filter { it.type != Type.HEADER } ?: emptyList()
            val aiRequest = aiService.requestPrompt(message, config, messages)
            return aiRequest.mapToMessage()
        }

        override suspend fun requestNewUserMessage(): Message? {
            val aiRequest = aiService.requestCustomPrompt(Prompts.NewUser.prompt, listOf(
                PromptConfig.AIntroduction,
                PromptConfig.BodyConfig(Message.getBody()),
                PromptConfig.SenderConfig(Sender.BOT.name),
                PromptConfig.TypeConfig(Type.NAME.name),
                PromptConfig.NoActions
            ))
            return aiRequest.mapToMessage()
        }

        override suspend fun generateNewMessageForAction(
            action: Action,
            message: String,
        ): Message? {
            return aiService.requestCustomPrompt(
                message,
                listOf(
                    PromptConfig.BodyConfig(Message.getBody()),
                ),
            ).mapToMessage()
        }

        override suspend fun requestSuggestionsMessage(): Message? {
            return aiService.requestCustomPrompt(
                Prompts.Suggestion.prompt,
                listOf(
                    PromptConfig.BodyConfig(Message.getBody()),
                    PromptConfig.SuggestionsConfig,
                    PromptConfig.SenderConfig(Sender.BOT.name),
                    PromptConfig.ArrayConfig(Message.getBody()),
                ),
            ).mapToMessage()
        }

    override suspend fun generatePromptForInput(
        value: String,
        config: List<PromptConfig>
    ): Message? {
        return aiService.requestCustomPrompt(
            value,
            listOf(
                PromptConfig.BodyConfig(Message.getBody()),
            ),
        ).mapToMessage()?.copy(type = Type.USER)
    }
}
