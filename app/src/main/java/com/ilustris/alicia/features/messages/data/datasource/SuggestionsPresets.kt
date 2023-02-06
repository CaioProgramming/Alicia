package com.ilustris.alicia.features.messages.data.datasource

import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.domain.model.Suggestion

object SuggestionsPresets {

    private val gainSuggestionsMessages = listOf(
        "Pagaram uma divída",
        "Din din caiu",
    )

    private val goalSuggestionsMessages = listOf(
        "Vou fazer um projetinho",
        "Quero comprar uma coisa"
    )

    val newUserSuggestions = listOf(Suggestion(Action.NAME))

    val commonSuggestions = listOf(
        Suggestion(Action.PROFIT),
        Suggestion(Action.LOSS),
        Suggestion(Action.GOAL),
        Suggestion(Action.HISTORY)
    )

}