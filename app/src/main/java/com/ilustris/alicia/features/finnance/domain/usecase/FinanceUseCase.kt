package com.ilustris.alicia.features.finnance.domain.usecase

import com.himanshoe.charty.circle.model.CircleData
import com.himanshoe.charty.line.model.LineData
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo
import com.ilustris.alicia.features.messages.data.model.Type
import kotlinx.coroutines.flow.Flow

interface FinanceUseCase {

    suspend fun saveMovimentation(description: String, value: String, tag: Tag, type: Type): Long

    suspend fun saveGoal(description: String, value: String, tag: Tag): Long

    suspend fun updateGoal(goal: Goal)
    fun getProfit(): Flow<List<MovimentationInfo>>
    fun getLoss(): Flow<List<MovimentationInfo>>
    fun getAmount(): Flow<Double>

    fun getAllMovimentations(): Flow<List<MovimentationInfo>>
    fun getMovimentationsByDay(): Flow<List<MovimentationInfo>>

    fun getMovimentationsChart(): Flow<List<LineData>>
    fun getMovimentationsCircleChart(): Flow<List<CircleData>>

    fun getGoals(): Flow<List<Goal>>

}