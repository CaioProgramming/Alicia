package com.ilustris.alicia.ai.model

import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.utils.emptyString

sealed class PromptConfig(
    val description: String,
) {
    data class BodyConfig(
        val body: String,
    ) : PromptConfig("Make sure the response is a JSON object with the following structure:$body")

    object KeepStructure : PromptConfig("Note: Keep the JSON property names. Do not translate them")

    data class CallBackConfig(
        val message: String,
    ) : PromptConfig(
            "return a callback for this message \"$message\"",
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
            "react that \"$data\" have been saved.",
        )

    object ActionConfig : PromptConfig(
        "Define a action and a value for this message",
    )

    object SuggestionsConfig : PromptConfig(
        "Return a list of relevant, humorous input hints in the first person." +
            "\nThe user can Perform one of the following actions: " +
            Action.entries
                .filter {
                    it != Action.NAME && it != Action.NONE
                }.joinToString { it.name } +
            "Provide personalized and playful hints that are tailored to the user's context," +
            "reflecting what they might want to do next, but with a humorous twist." +
            "Do not just return the enum values, but a human-readable description of the actions," +
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
            "Format this data \"value\": \"$value\".",
        )

    data class HumorConfig(
        val humor: Humors = Humors.entries.random(),
    ) : PromptConfig(
            "Use a teenage girl ${humor.name.toLowerCase()} tone for your response." +
                "\nEnsure that ${humor.description.toLowerCase()}\nYou are free to use emojis",
        )

    object KeepOnContext : PromptConfig(
        "Keep the context of the request in mind when responding" +
            "Ensure all suggestions are relevant to the financial app, even if the tone is playful. " +
            "The AI should feel like a helpful financial assistant," +
            "remember you are the AI named Alicia, and you are a financial assistant",
    )

    object FeaturesExamples : PromptConfig(
        "Tell the user about what he can do in the app like: " +
            "You can add a new expense, check your balance, or set a new goal.",
    )

    data class ErrorExplanation(
        val message: String,
    ) : PromptConfig(
            "Explain that $message",
        )

    object ExtractValuableDataConfig : PromptConfig(
        "Extract valuable data like the description, the value and the possible tags from the message",
    )
}

enum class Humors(
    val description: String = emptyString(),
) {
    ANGRY("Respond in an annoyed, frustrated tone, as if everything is the worst."),
    BORED("Respond in a disinterested, uninterested tone, like you're over it."),
    SARCASTIC("Respond with heavy sarcasm, making it clear you're not taking things seriously."),
    IRONIC("Respond with a tone of ironic appreciation, subtly showing the opposite of what you mean."),
    SASSY("Respond with a sharp, cutting tone, as if you're too cool for this."),
    SNARKY("Respond with a tone of mocking or cynical humor, as if you're making fun of the situation."),
    DRAMATIC("Respond with an exaggerated, over-the-top tone, as if everything is a big deal."),
    IMPATIENT("Respond with a hurried, irritated tone, as if you can't wait for this to be over."),
    JUDGMENTAL("Respond with a critical, disapproving tone, as if you're looking down on the situation."),
    WHINY("Respond with a complaining, high-pitched tone, as if you're not happy with anything."),
    ENTHUSIASTIC("Respond with an overly excited, eager tone, as if everything is amazing."),
    GOSSIPY("Respond with a tone of sharing juicy details, as if you're spilling the tea."),
    FLIRTY("Respond with a playful, teasing tone, as if you're trying to charm the other person."),
    INDIFFERENT("Respond with a tone of complete lack of interest, as if you couldn't care less."),
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
