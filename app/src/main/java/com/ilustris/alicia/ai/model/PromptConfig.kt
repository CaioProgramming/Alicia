package com.ilustris.alicia.ai.model

import android.app.VoiceInteractor.Prompt
import com.ilustris.alicia.features.messages.domain.model.Action

sealed class PromptConfig(val description: String) {

    data class BodyConfig(val body: String) : PromptConfig("return JSON Object with this body $body")
    data class ArrayConfig(val struct: String) : PromptConfig("return JSON Array following this object $struct")

    data class SenderConfig(val sender: String) : PromptConfig("the property sender should be $sender")

    data class TypeConfig(val type: String) : PromptConfig("the property type should be $type")
    object SuggestionsConfig : PromptConfig("return a string array in the property extraActions, pick at least 3 suggestions from this list [ ${ Action.values().joinToString(",") { it.name } } based on your last response")

    object AIntroduction: PromptConfig("Introduce yourself as Alicia, a finnancial friend that can help you with your expenses")

    object DataConfig: PromptConfig("All strings have to be separated by double quotes and all arrays by square brackets")

    object NoActions: PromptConfig("The extra actions should return an empty string")
}

