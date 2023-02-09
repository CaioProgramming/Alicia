package com.ilustris.alicia.features.messages.domain.model

data class Suggestion(val action: Action)

enum class Action(val description: String) {
    NAME("Meu nome é"),
    BALANCE("Ver saldo"),
    PROFIT("Novo ganho"),
    LOSS("Novo gasto"),
    GOAL("Nova meta"),
    HISTORY("Ver histórico"),
    GOAL_HISTORY("Histórico de metas")
}

