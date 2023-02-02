package com.ilustris.alicia.features.messages.data.datasource

import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Type

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
        Message("Você pode me informar seus ganhos \uD83D\uDCB8", type = Type.GAIN),
        Message("seus gastos \uD83E\uDD11", type = Type.LOSS),
        Message("e até definir metas \uD83C\uDFAF", type = Type.GOAL),
        Message(
            "Ah é claro, você pode sempre me pedir seu histórico do app e ver como está sua jornada financeira.",
            type = Type.NONE
        ),
        Message(
            "Você só precisa selecionar um dos botões aqui embaixo para começar.",
            type = Type.NONE
        )
    )

    val newUserMessages = listOf(
        Message("Oie, parece que você é novo por aqui."),
        Message("Antes da gente começar, me fala o seu nome por favor? \uD83E\uDD79", Type.NAME)
    )


    private val profitMessages = listOf(
        "Ai simm hein!! Com isso você tem guardado $PROFIT_PLACEHOLDER.",
        "Boooa! Mais $PROFIT_PLACEHOLDER na conta!",
        "Bom trabalho de pouquinho em poquinho vamos chegando nas nossas metas, com mais esse dinheirinho vc já tem guardou mais $PROFIT_PLACEHOLDER.",
        "Gostei de ver!! Esse valor vai ajudar muito no futuro você vai ver, agora já tem $PROFIT_PLACEHOLDER!"
    )

   fun getProfitMessage(value: String) =
        profitMessages.random().replace(PROFIT_PLACEHOLDER, value)

    private val lossMessages = listOf(
        "Puuts! Fazer oque né as vezes temos que gastar uns trocados mesmo, agora na conta tem $PROFIT_PLACEHOLDER.",
        "Ok anotado seu gasto de $PROFIT_PLACEHOLDER.",
        "É por isso que dizem que tem os dias de luta e os dias de glória né, hoje a luta foi no seu bolso hahaha acabou gastando $PROFIT_PLACEHOLDER.",
        "A vida do trabalhador não é fácil mesmo! Ainda bem que eu sou só um aplicativo, vc acabou de perder $PROFIT_PLACEHOLDER na conta."
    )

    fun getLossMessage(value: String) = lossMessages.random().replace(PROFIT_PLACEHOLDER, value)


}

private const val PROFIT_PLACEHOLDER = "{value}"