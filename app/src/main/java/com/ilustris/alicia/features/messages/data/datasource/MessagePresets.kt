package com.ilustris.alicia.features.messages.data.datasource

import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.messages.data.datasource.presets.BillsPresets
import com.ilustris.alicia.features.messages.data.datasource.presets.DefaultPresets
import com.ilustris.alicia.features.messages.data.datasource.presets.ShoppingPresets
import com.ilustris.alicia.features.messages.data.datasource.presets.UserPresets
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Type
import com.ilustris.alicia.features.messages.domain.model.Action

object MessagePresets {

    fun getGreeting(name: String) = Message(UserPresets.getGreetingMessage(name))

    val introductionMessages = listOf(
        Message("Você pode me ver como quiser, amiga, conhecida, sua ex... Mas meu objetivo aqui é te ajudar com sua vida financeira, podemos dizer que um diário de finanças né."),
        Message(
            "Nessa conversa você pode me falar de tudo, seus rendimentos, gastos e até metas desde comprar um pastel na feira até comprar um apartamento, vou te ajudar a acompanhar tudo da melhor forma possível.",
            type = Type.NONE,
            extraActions = listOf(Action.PROFIT.name, Action.LOSS.name, Action.GOAL.name).toString()
        ),
        Message(
            "Ah é claro, você pode sempre me pedir seu histórico do app e ver como está sua jornada financeira.",
            type = Type.NONE,
            extraActions = listOf(Action.HISTORY.name).toString()
        ),
    )

    val newUserMessages = listOf(
        Message("Oie, parece que você é novo por aqui."),
        Message("Antes da gente começar, me fala o seu nome por favor? \uD83E\uDD79", Type.NONE)
    )

    fun getProfitMessage(value: String, tag: Tag): Message {
        return Message(
            when (tag) {
                Tag.BILLS -> BillsPresets().getProfitMessage(value)
                Tag.SHOPPING -> ShoppingPresets().getProfitMessage(value)
                else -> DefaultPresets().getProfitMessage(value)
            },
            extraActions = listOf(
                Action.LOSS.name,
                Action.GOAL.name,
                Action.HISTORY.name,
                Action.GOAL_HISTORY.name,
            ).toString()
        )
    }

    fun getLossMessage(value: String, tag: Tag): Message {
        return Message(
            when (tag) {
                Tag.BILLS -> BillsPresets().getLossMessage(value)
                Tag.SHOPPING -> ShoppingPresets().getLossMessage(value)
                else -> DefaultPresets().getLossMessage(value)
            },
            extraActions = listOf(
                Action.PROFIT.name,
                Action.GOAL.name,
                Action.HISTORY.name,
                Action.GOAL_HISTORY.name,
            ).toString()
        )
    }

    fun getGoalMessage(tag: Tag, description: String) =
        Message(DefaultPresets().getGoalMessage(description))

    fun keepGoingMessage() = Message(
        "Me fala ai como estão as coisas?",
        extraActions = listOf(
            Action.PROFIT.name,
            Action.LOSS.name,
            Action.GOAL.name,
        ).toString()
    )
}

