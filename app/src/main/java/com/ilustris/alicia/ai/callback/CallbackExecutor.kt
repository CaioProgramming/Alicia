package com.ilustris.alicia.ai.callback

import com.ilustris.alicia.features.messages.domain.model.Action

interface CallbackExecutor {
    suspend fun execute(message: String): Pair<Action, Any>?
}
