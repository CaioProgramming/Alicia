package com.ilustris.alicia.features.messages.domain.model

import com.ilustris.alicia.features.messages.data.model.Type

data class Suggestion(val name: String, val action: Action)

enum class Action {
    NAME, BALANCE, PROFIT, LOSS, GOAL, HISTORY, PROFIT_HISTORY, LOSS_HISTORY, GOAL_HISTORY
}

