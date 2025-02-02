package com.ilustris.alicia.features.messages.domain.usecase

import com.ilustris.alicia.ai.usecase.RequestResult
import com.ilustris.alicia.features.messages.domain.model.Action

interface ChatDataManager {
    suspend fun saveData(callback: Pair<Action, Any>): RequestResult<Exception, Long?>
}
