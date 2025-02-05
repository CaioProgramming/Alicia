package com.ilustris.alicia.ai.message

import com.ilustris.alicia.ai.model.PromptConfig
import com.ilustris.alicia.ai.model.ai.AIResponse
import com.ilustris.alicia.ai.model.buildPrompt
import com.ilustris.alicia.ai.usecase.AIUseCase
import com.ilustris.alicia.features.messages.data.model.Type
import javax.inject.Inject

class MessageGeneratorImpl
    @Inject
    constructor(
        private val aiUseCase: AIUseCase,
    ) : MessageGenerator {
        private val humor = PromptConfig.HumorConfig()

        override suspend fun replyMessage(
            userName: String,
            message: String,
        ): AIResponse? {
            val response =
                aiUseCase.generateResponse(
                    prompt =
                        buildPrompt {
                            addPrompt(PromptConfig.ReplyConfig(message, userName).description)
                            addPrompt(humor.description)
                        },
                    clazz = AIResponse::class.java,
                    requireTranslation = true,
                    useContext = true,
                )
            return try {
                response.success.value
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override suspend fun generateMessage(
            prompt: String,
            useTypes: Boolean,
            humorEnabled: Boolean,
        ): AIResponse? {
            val typeReplacement = if (useTypes) Type.entries.joinToString(("|")) else "null"

            val response =
                aiUseCase.generateResponse(
                    prompt =
                        buildPrompt {
                            addPrompt(prompt)
                            if (humorEnabled) {
                                addPrompt(humor.description)
                            }
                        },
                    clazz = AIResponse::class.java,
                    requireTranslation = true,
                    useContext = true,
                    specificReplacement = Pair("type", typeReplacement),
                )
            return try {
                response.success.value
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
