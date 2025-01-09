package com.ilustris.alicia.ai.usecase

import android.util.Log
import com.ilustris.alicia.ai.mapper.mapTo
import com.ilustris.alicia.ai.model.PromptBuilder
import com.ilustris.alicia.ai.model.PromptConfig
import com.ilustris.alicia.ai.service.AIService
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.utils.toJsonSchema
import javax.inject.Inject

class AIUseCaseImpl
    @Inject
    constructor(
        private val aiService: AIService,
    ) : AIUseCase {
        override suspend fun <T> generateResponse(
            prompt: PromptBuilder,
            clazz: Class<T>,
            specificReplacement: Pair<String, String>?,
            requireTranslation: Boolean,
        ): RequestResult<Exception, T> {
            try {
                prompt.addPrompt(
                    PromptConfig
                        .BodyConfig(
                            toJsonSchema(clazz, specificReplacement)
                                .replaceClassIdentifier(
                                    Action::class.java.simpleName,
                                    "${Action.entries.joinToString("|") { it.name }}",
                                ).replaceClassIdentifier(
                                    Tag::class.java.simpleName,
                                    "${Tag.entries.joinToString("|") { it.name }}",
                                ),
                        ).description,
                )
                prompt.addPrompt(PromptConfig.KeepStructure.description)
                prompt.addPrompt(PromptConfig.DataConfig.description)
                prompt.addPrompt(PromptConfig.KeepOnContext.description)
                val aiRequest = aiService.requestCustomPrompt(prompt.build(), requireTranslation)

                val aiResponse = aiRequest.success.value
                val data = aiResponse.mapTo<T>(clazz) ?: return RequestResult.Error(Exception("Error mapping response"))
                return RequestResult.Success(data)
            } catch (e: Exception) {
                e.printStackTrace()
                return RequestResult.Error(e)
            }
        }
    }

fun String.removeStable(): String {
    return return this.replace(Regex("\"\$stable: int"), "")
}

fun String.replaceClassIdentifier(
    clazzName: String,
    replacement: String,
): String {
    Log.w(javaClass.simpleName, "replaceEnumIdentifier: Replacing $clazzName with $replacement")
    if (!contains(clazzName)) {
        return this
    }
    return this.replace(Regex(clazzName), "$replacement")
}
