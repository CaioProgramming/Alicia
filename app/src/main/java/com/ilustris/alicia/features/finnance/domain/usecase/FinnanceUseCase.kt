package com.ilustris.alicia.features.finnance.domain.usecase

import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.messages.data.model.Message
import com.ilustris.alicia.features.messages.data.model.Type
import kotlinx.coroutines.flow.Flow

interface FinnanceUseCase {

    suspend fun saveMovimentation(description: String, value: String, type: Type): Long

    fun getMovimentations() : Flow<List<Movimentation>>

    fun getGoals() : Flow<List<Goal>>

}