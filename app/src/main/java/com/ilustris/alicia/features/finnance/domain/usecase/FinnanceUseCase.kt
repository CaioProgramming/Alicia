package com.ilustris.alicia.features.finnance.domain.usecase

import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo
import com.ilustris.alicia.features.messages.data.model.Type
import kotlinx.coroutines.flow.Flow

interface FinnanceUseCase {

    suspend fun saveMovimentation(description: String, value: String, tag: Tag, type: Type): Long

    fun getProfit(): Flow<List<MovimentationInfo>>
    fun getLoss(): Flow<List<MovimentationInfo>>
    fun getAmount(): Flow<Double>

    fun getAllMovimentations(): Flow<List<Movimentation>>

    fun getGoals(): Flow<List<Goal>>

}