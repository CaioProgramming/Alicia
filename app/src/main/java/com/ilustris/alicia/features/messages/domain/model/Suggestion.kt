package com.ilustris.alicia.features.messages.domain.model

data class Suggestion(val action: Action)

enum class Action(val description: String) {
    NAME("$PROMPT_INTRODUCTION saying thar your name is [username]"),
    BALANCE("$PROMPT_INTRODUCTION asking for your balance"),
    PROFIT("$PROMPT_INTRODUCTION about that new profit [message]"),
    LOSS("$PROMPT_INTRODUCTION about that new loss [message]"),
    GOAL("$PROMPT_INTRODUCTION about your new goal [message]"),
    HISTORY("$PROMPT_INTRODUCTION about your expenses"),
    GOAL_HISTORY("$PROMPT_INTRODUCTION about your goal history")

}

fun Action.replacePlaceHolder(value: String): String {
   return when(this) {
        Action.NAME -> this.description.replace("[username]", value)
        Action.BALANCE, Action.HISTORY, Action.GOAL_HISTORY -> this.description
        Action.PROFIT, Action.LOSS, Action.GOAL -> this.description.replace("[message]", value)
    }
}

private const val PROMPT_INTRODUCTION = "Generate a kind message "
