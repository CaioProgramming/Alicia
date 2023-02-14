package com.ilustris.alicia.features.messages.data.datasource

import android.icu.util.Calendar
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
        Message("Deixa eu me apresentar... Eu sou a Alicia, sua assistente financeira 😁!"),
        Message("Vou ate ajudar o máximo que puder por aqui, você só precisa me informar regularmente e logo vamos ter dados mais completos."),
        Message("Não se preocupe, tudo que me mandar, fica entre nós. Você só fala o que quiser e quando quiser, sem pressão..."),
        Message("Ah é você também não precisa se preocupar com a internet para salvar seus rendimentos e gastos, a mãe ta on e off sempre hahaa 🤪."),
        Message(
            "Você só precisa clicar em uma das ações abaixo pra começar seja rendimento ou gasto. Sem julgamento nem nada do tipo sinta-se a vontade.",
            type = Type.NONE,
            extraActions = listOf(Action.PROFIT.name, Action.LOSS.name).toString()
        ),
        Message(
            "Você também pode criar metas como comprar um pastel na feira ou um apartamento, vou te ajudar a acompanhar tudo da melhor forma possível",
            extraActions = listOf(Action.GOAL.name).toString()
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

    fun dayIntroduction(userName: String): String {
        val dayHour = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
        if (dayHour in 6..12) {
            return "Bom dia $userName\n"
        } else if (dayHour in 13..18) {
            return "Boa tarde $userName"
        } else {
            return "Boa noite $userName"
        }
    }


}

