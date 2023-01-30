package com.ilustris.alicia.features.messages.data.datasource

import com.ilustris.alicia.features.messages.data.model.Type
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

    val newUserSuggestions = listOf(Suggestion("Você pode me chamar de ", Action.NAME))

    fun commonSuggestions() = listOf(
        Suggestion("Novo ganho", Action.PROFIT),
        Suggestion("Novo gasto", Action.LOSS),
        Suggestion("Nova meta", Action.GOAL),
        Suggestion("Ver histórico", Action.HISTORY))

}