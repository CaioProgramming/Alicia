package com.ilustris.alicia.features.messages.data.datasource

import com.ilustris.alicia.features.messages.data.model.Action
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.domain.model.Suggestion

object MessagePresets {

    fun getGreeting(name: String) = greetingMessages(name).random()

    private fun greetingMessages(name: String) = listOf(
        Message("Oie $name"),
        Message("Ei $name, tudo bem?"),
        Message("Oiii $name."),
        Message("Salve salve $name"),
        Message("Olá olá $name"),
        Message("Você de novo $name?")
    )


    val introductionMessages = listOf(
        Message("Estou aqui para te ajudar a organizar sua vida financeira, de uma forma mais prática através dessa conversa."),
        Message("Você pode me informar seus ganhos \uD83D\uDCB8", action = Action.GAIN),
        Message("seus gastos também \uD83E\uDD11", action = Action.LOSS),
        Message("e até definir metas \uD83C\uDFAF", action = Action.GOAL)
    )

    val newUserMessages = listOf(
        Message("Oie, parece que você é novo por aqui"),
        Message("Antes da gente começar, me fala o seu nome por favor? \uD83E\uDD79", Action.NAME),
        Message("ps: clica na minha última mensagem \uD83D\uDE17")
    )


    val newUserSuggestions = listOf(Suggestion("Meu nome é...", Action.NAME))

    val commonSuggestions = listOf(Suggestion("Hoje eu comprei...", Action.LOSS), Suggestion("Hoje eu ganhei...", Action.GAIN))


}