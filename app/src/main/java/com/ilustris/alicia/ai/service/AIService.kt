package com.ilustris.alicia.ai.service

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content
import com.ilustris.alicia.BuildConfig
import com.ilustris.alicia.ai.model.PromptConfig
import com.ilustris.alicia.ai.model.Prompts
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Sender
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.domain.model.Action
import java.util.Locale

class AIService {

    val model = GenerativeModel(modelName = BuildConfig.AIMODEL, apiKey = BuildConfig.AIKEY)

    suspend fun requestCustomPrompt(prompt: String, config: List<PromptConfig>) : GenerateContentResponse {
        val chatModel =  model.startChat(emptyList())
        val options = config.plus(PromptConfig.DataConfig).joinToString("\n") { it.description }
        val language = modelLanguage()
        val request = "$prompt\n$options\n$language"
        Log.d(javaClass.simpleName, "prompt request ➡️ $request")
        return chatModel.sendMessage(request)
    }

    suspend fun requestPrompt(prompt: String,
                              config: List<PromptConfig>,
                              chat: List<Message>) : GenerateContentResponse {

        Log.i(javaClass.simpleName, "requestPrompt: Initiating chat with prompt $prompt")
        val aiChats = chat.filter { it.sender == Sender.BOT }.map {
            content(role = "model") { text(it.message) }
        }
        val userChats = chat.filter { it.sender == Sender.USER }.map {
            content(role = "user") { text(it.message) }
        }
        val history = listOf(
            aiChats, userChats
        )
        val chatModel =  model.startChat(
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
        return "All texts are in ${locale.language} language."
    }

}