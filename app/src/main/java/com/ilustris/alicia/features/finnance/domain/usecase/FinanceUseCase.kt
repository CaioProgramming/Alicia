package com.ilustris.alicia.features.finnance.domain.usecase

import com.himanshoe.charty.circle.model.CircleData
import com.himanshoe.charty.line.model.LineData
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.domain.data.GoalInfo
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo
import com.ilustris.alicia.features.messages.data.model.Type
import kotlinx.coroutines.flow.Flow

interface FinanceUseCase {
    suspend fun saveMovimentation(
        description: String,
        value: String,
        tag: Tag,
        type: Type,
    ): Movimentation

    suspend fun saveMovimentation(movimentation: Movimentation): Long

    suspend fun deleteMovimentation(movimentation: Movimentation)

    fun getProfit(): Flow<List<MovimentationInfo>>

    fun getLoss(): Flow<List<MovimentationInfo>>

    fun getAmount(): Flow<Double>

    fun getAmountSync(): Double

    fun getAllMovimentations(): Flow<List<MovimentationInfo>>

    fun getAllMovimentationsSync(): List<MovimentationInfo>

    suspend fun getMovimentationById(id: Long): Flow<Movimentation>

    suspend fun getMovimentationByIdSync(id: Long): Movimentation

    fun getMovimentationsByDay(): Flow<List<MovimentationInfo>>

    fun getMovimentationsChart(): Flow<List<LineData>>

    fun getMovimentationsCircleChart(): Flow<List<CircleData>>

    suspend fun saveGoal(
        description: String,
        value: String,
        tag: Tag,
    ): Long

    suspend fun saveGoal(goal: Goal): Long

    suspend fun updateGoal(goal: Goal)

    fun getGoals(): Flow<List<Goal>>

    fun getGoalById(id: Long): Flow<Goal>

    fun getGoalByIdSync(id: Long): Goal

    fun getGoalsInfo(): Flow<List<GoalInfo>>
}
