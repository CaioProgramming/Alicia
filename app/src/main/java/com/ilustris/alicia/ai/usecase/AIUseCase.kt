package com.ilustris.alicia.ai.usecase

import com.ilustris.alicia.ai.model.PromptConfig
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.domain.model.Action
import kotlinx.coroutines.flow.Flow

interface AIUseCase {

   var messageHistory: Flow<List<Message>>

   suspend fun requestMessage(message: String, config: List<PromptConfig>) : Message?

   suspend fun requestNewUserMessage() : Message?

   suspend fun generateNewMessageForAction(action: Action, message: String): Message?

   suspend fun requestSuggestionsMessage() : Message?

   suspend fun generatePromptForInput(value: String, config: List<PromptConfig>) : Message?

}