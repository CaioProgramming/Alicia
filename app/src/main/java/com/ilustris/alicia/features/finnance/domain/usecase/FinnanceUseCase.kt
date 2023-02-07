package com.ilustris.alicia.features.finnance.domain.usecase

import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo
import com.ilustris.alicia.features.messages.data.model.Type
import kotlinx.coroutines.flow.Flow

interface FinnanceUseCase {

    suspend fun saveMovimentation(description: String, value: String, tag: Tag, type: Type): Long

    suspend fun saveGoal(description: String, value: String, tag: Tag): Long
    fun getProfit(): Flow<List<MovimentationInfo>>
    fun getLoss(): Flow<List<MovimentationInfo>>
    fun getAmount(): Flow<Double>

    fun getAllMovimentations(): Flow<List<MovimentationInfo>>

    fun getGoals(): Flow<List<Goal>>

}