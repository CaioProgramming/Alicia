package com.ilustris.alicia.features.finnance.domain.usecase

import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.domain.repository.FinnanceRepository
import com.ilustris.alicia.features.messages.data.model.Type
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FinnanceUseCaseImpl @Inject constructor(private val finnanceRepository: FinnanceRepository) :
    FinnanceUseCase {

    override suspend fun saveMovimentation(description: String, value: String, type: Type): Long {
        val doubleValue = value.toDouble() / 100
        val decimalValue = if (type == Type.GAIN) doubleValue else doubleValue.unaryMinus()
        val movimentation = Movimentation(value = decimalValue, description = description)
        return finnanceRepository.saveMovimentation(movimentation)
    }

    override fun getProfit(): Flow<List<Movimentation>> = flow {
        finnanceRepository.getMovimentations().collect {
            emit(it.filter { movimentation -> movimentation.value > 0 })
        }
    }
    override fun getLoss(): Flow<List<Movimentation>> = flow {
        finnanceRepository.getMovimentations().collect {
            emit(it.filter { movimentation -> movimentation.value < 0 })
        }
    }

    override fun getAmount(): Flow<Double> = flow {
        finnanceRepository.getMovimentations().collect {
            emit(it.sumOf { movimentation -> movimentation.value })
        }
    }

    override fun getAllMovimentations(): Flow<List<Movimentation>> = finnanceRepository.getMovimentations()

    override fun getGoals(): Flow<List<Goal>> {
        TODO("Not yet implemented")
    }

}