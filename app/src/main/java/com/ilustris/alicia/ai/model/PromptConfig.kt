package com.ilustris.alicia.ai.model

import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.utils.emptyString

sealed class PromptConfig(
    val description: String,
) {
    data class BodyConfig(
        val body: String,
    ) : PromptConfig("Make sure the response is a JSON object with the following structure:$body")

    object KeepStructure : PromptConfig("Note: Keep the JSON property names. Do not translate them. Do not comment on structure")

    data class CallBackConfig(
        val message: String,
    ) : PromptConfig(
            "Choose an action to app perform for this message: \"$message\"",
        )

    data class ReplyConfig(
        val message: String,
        val userName: String,
    ) : PromptConfig(
            "$userName sent a message \"$message\". Reply to this message",
        )

    data class CallBackSuccessConfig(
        val data: String,
    ) : PromptConfig(
            "Send a message saying that \"$data\" have been saved successfully" +
                "Don't expose the data, extract the key information and return a human-readable message",
        )

    object ActionConfig : PromptConfig(
        "You have theses actions to choose:\n" +
            Action.entries.joinToString(".\n-") { it.name },
    )

    object SuggestionsConfig : PromptConfig(
        "Return a humorous list of autocomplete suggestions." +
            "\nThe user can Perform one of the following actions:\n" +
            Action.entries
                .filter {
                    it != Action.NAME && it != Action.NONE
                }.joinToString(".\n-") { it.name } +
            "\nDo not just return the enum values, but a human-readable description of the actions," +
            "Ensure the suggestions are phrased as incomplete sentences or input hints that the user can easily complete",
    )

    object AIntroduction : PromptConfig(
        "Introduce yourself as Alicia." +
            "A finnancial friend that can help you with your expenses",
    )

    object NameConfig : PromptConfig("Send a message asking for the user's name")

    object DataConfig : PromptConfig(
        "Note: Ensure that all string values" +
            " in the JSON use double quotes (\") and not single quotes (')",
    )

    data class MessageResourceConfig(
        val message: String,
    ) : PromptConfig(
            "Use this message as resource to improve your response \"$message\"",
        )

    data class FormatResponseConfig(
        val value: String,
    ) : PromptConfig(
            "map this message: \"$value\"",
        )

    data class HumorConfig(
        val humor: Humors = Humors.entries.random(),
    ) : PromptConfig(
            "Use a teenage girl ${humor.name.toLowerCase()} tone for your response." +
                "\nEnsure that ${humor.description.toLowerCase()}\nYou are free to use emojis",
        )

    object KeepOnContext : PromptConfig(
        "Keep the context of the request in mind when responding. " +
            "Ensure all suggestions are relevant to the financial app, even if the tone is playful. " +
            "The AI should feel like a helpful financial assistant," +
            "remember you are the AI named Alicia, and you are a financial assistant",
    )

    data class ErrorExplanation(
        val message: String,
    ) : PromptConfig(
            "Explain that $message",
        )

    object ExtractValuableDataConfig : PromptConfig(
        "Extract key values the possible tags from the message",
    )

    object FeaturesExamples : PromptConfig(
        "Tell the user about what he can do in the app like: " +
            "You can add a new expense, check your balance, or set a new goal.",
    )
}

enum class Humors(
    val description: String = emptyString(),
) {
    SARCASTIC("Respond with heavy sarcasm, making it clear you're not taking things seriously."),
    SASSY("Respond with a sharp, cutting tone, as if you're too cool for this."),
    FLIRTY("Respond with a playful, teasing tone, as if you're trying to charm the other person."),
}

class PromptBuilder {
    private val prompts = mutableListOf<String>()

    fun addPrompt(prompt: String) {
        prompts.add(prompt)
    }

    fun build(): String =
        prompts
            .mapIndexed { index, s ->
                "${index + 1}. $s."
            }.joinToString("\n")
}

fun buildPrompt(init: PromptBuilder.() -> Unit): PromptBuilder {
    val builder = PromptBuilder()
    builder.init()
    return builder
}
