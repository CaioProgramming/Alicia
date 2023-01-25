package com.ilustris.alicia.features.finnance.domain.usecase

import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.domain.repository.FinnanceRepository
import com.ilustris.alicia.features.messages.data.model.Type
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FinnanceUseCaseImpl @Inject constructor(private val finnanceRepository: FinnanceRepository) :
    FinnanceUseCase {

    override suspend fun saveMovimentation(description: String, value: String, type: Type): Long {
        val doubleValue = value.toDouble() / 100
        val decimalValue = if (type == Type.GAIN) doubleValue else doubleValue.unaryMinus()
        val movimentation = Movimentation(value = decimalValue, description = description)
        return finnanceRepository.saveMovimentation(movimentation)
    }

    override fun getMovimentations(): Flow<List<Movimentation>> = finnanceRepository.getMovimentations()

    override fun getGoals(): Flow<List<Goal>> {
        TODO("Not yet implemented")
    }

}