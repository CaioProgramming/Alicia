package com.ilustris.alicia.ai.service

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content
import com.ilustris.alicia.BuildConfig
import com.ilustris.alicia.ai.model.PromptConfig
import com.ilustris.alicia.ai.usecase.RequestResult
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Sender
import com.ilustris.alicia.utils.emptyString
import java.util.Locale

class AIService {
    val model = GenerativeModel(modelName = BuildConfig.AIMODEL, apiKey = BuildConfig.AIKEY)

    suspend fun requestCustomPrompt(
        prompt: String,
        requireTranslation: Boolean,
    ): RequestResult<Exception, GenerateContentResponse> {
        try {
            val chatModel = model.startChat(emptyList())

            val language = if (requireTranslation) modelLanguage() else emptyString()
            val request = "$prompt\n$language"
            Log.i(javaClass.simpleName, "prompt request ➡️ \n$request\n")

            return RequestResult.Success(chatModel.sendMessage(request))
        } catch (e: Exception) {
            e.printStackTrace()
            return RequestResult.Error(e)
        }
    }

    suspend fun requestPrompt(
        prompt: String,
        config: List<PromptConfig>,
        chat: List<Message>,
    ): GenerateContentResponse {
        Log.i(javaClass.simpleName, "requestPrompt: Initiating chat with prompt $prompt")
        val aiChats =
            chat.filter { it.sender == Sender.BOT }.map {
                content(role = "model") { text(it.message) }
            }
        val userChats =
            chat.filter { it.sender == Sender.USER }.map {
                content(role = "user") { text(it.message) }
            }
        val history =
            listOf(
                aiChats,
                userChats,
            )
        val chatModel =
            model.startChat(
                history = history.flatten(),
            )

        val configuration = config.plus(PromptConfig.DataConfig).plus(PromptConfig.DataConfig).joinToString(",") { it.description }
        val request = "$prompt\n$configuration\n${modelLanguage()}"
        Log.d(javaClass.simpleName, "prompt request ➡️ $request")
        return chatModel.sendMessage(request)
    }

    private fun modelLanguage(): String {
        val locale = Locale.getDefault()
        locale.language
        return "All responses must be in ${locale.displayLanguage} (${locale.language})."
    }
}
