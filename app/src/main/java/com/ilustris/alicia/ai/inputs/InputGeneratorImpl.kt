package com.ilustris.alicia.ai.inputs

import com.ilustris.alicia.ai.model.PromptConfig
import com.ilustris.alicia.ai.model.ai.AISuggestions
import com.ilustris.alicia.ai.model.buildPrompt
import com.ilustris.alicia.ai.usecase.AIUseCase
import javax.inject.Inject

class InputGeneratorImpl
    @Inject
    constructor(
        private val aiUseCase: AIUseCase,
    ) : InputGenerator {
        override suspend fun generateInputs(): List<String> {
            return emptyList()
            val suggestionsResponse =
                aiUseCase
                    .generateResponse(
                        buildPrompt {
                            addPrompt(PromptConfig.SuggestionsConfig.description)
                        },
                        AISuggestions::class.java,
                        specificReplacement = Pair("suggestions", "List<String>"),
                    )

            return if (suggestionsResponse.isFailure()) {
                emptyList()
            } else {
                suggestionsResponse.success.value.suggestions
            }
        }
    }
