package com.ilustris.alicia.ai.callback

import com.ilustris.alicia.ai.model.PromptConfig
import com.ilustris.alicia.ai.model.ai.AICallBack
import com.ilustris.alicia.ai.model.buildPrompt
import com.ilustris.alicia.ai.usecase.AIUseCase
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.domain.model.bodyClass
import javax.inject.Inject

class CallbackExecutorImpl
    @Inject
    constructor(
        private val aiUseCase: AIUseCase,
    ) : CallbackExecutor {
        override suspend fun execute(message: String): Pair<Action, Any>? {
            val callBackResponse =
                aiUseCase
                    .generateResponse(
                        buildPrompt {
                            addPrompt(PromptConfig.CallBackConfig(message).description)
                            addPrompt(PromptConfig.ActionConfig.description)
                        },
                        AICallBack::class.java,
                        requireTranslation = false,
                        useContext = false,
                    )

            if (callBackResponse.isFailure()) {
                return null
            } else {
                val callBack = callBackResponse.success.value
                return handleCallBack(callBack, message)
            }
        }

        private suspend fun handleCallBack(
            callBack: AICallBack,
            supportMessage: String,
        ): Pair<Action, Any>? {
            val callBackData =
                aiUseCase
                    .generateResponse(
                        buildPrompt {
                            addPrompt(PromptConfig.FormatResponseConfig(supportMessage).description)
                            addPrompt(PromptConfig.ExtractValuableDataConfig.description)
                        },
                        callBack.action.bodyClass(),
                        requireTranslation = false,
                        specificReplacement = Pair("tag", Tag.entries.joinToString("|")),
                    )
            return if (callBackData.isFailure()) {
                null
            } else {
                Pair(callBack.action, callBackData.success.value)
            }
        }
    }
