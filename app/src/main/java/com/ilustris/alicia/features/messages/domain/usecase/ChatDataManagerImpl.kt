package com.ilustris.alicia.features.messages.domain.usecase

import com.ilustris.alicia.ai.usecase.RequestResult
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.domain.usecase.FinanceUseCase
import com.ilustris.alicia.features.messages.domain.model.Action
import com.ilustris.alicia.features.messages.domain.model.NameBody
import com.ilustris.alicia.features.user.domain.usecase.UserUseCase
import javax.inject.Inject

class ChatDataManagerImpl
    @Inject
    constructor(
        private val userUseCase: UserUseCase,
        private val financeUseCase: FinanceUseCase,
    ) : ChatDataManager {
        override suspend fun saveData(callback: Pair<Action, Any>): RequestResult<Exception, Long?> {
            try {
                val taskResult =
                    when (callback.first) {
                        Action.NAME -> saveUser((callback.second as NameBody))
                        Action.PROFIT, Action.LOSS ->
                            saveMovimentation(
                                (callback.second as Movimentation),
                                callback.first,
                            )

                        Action.GOAL -> saveGoal(callback.second as Goal)
                        else -> null
                    }
                return RequestResult.Success(taskResult)
            } catch (e: Exception) {
                e.printStackTrace()
                return RequestResult.Error(e)
            }
        }

        private suspend fun saveMovimentation(
            movimentation: Movimentation,
            action: Action,
        ): Long {
            val movimentationFormatted =
                movimentation
                    .copy(id = 0, value = if (action == Action.LOSS) movimentation.value.unaryMinus() else movimentation.value.unaryPlus())
            return financeUseCase.saveMovimentation(movimentationFormatted)
        }

        private suspend fun saveGoal(goal: Goal) = financeUseCase.saveGoal(goal)

        private suspend fun saveUser(user: NameBody) = userUseCase.saveUser(user.name)
    }
