package com.ilustris.alicia.features.finnance.domain.usecase

import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo
import com.ilustris.alicia.features.finnance.domain.mapper.MovimentationMapper
import com.ilustris.alicia.features.finnance.domain.repository.FinnanceRepository
import com.ilustris.alicia.features.messages.data.model.Type
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class FinnanceUseCaseImpl @Inject constructor(
    private val finnanceRepository: FinnanceRepository,
    private val movimentationMapper: MovimentationMapper
) :
    FinnanceUseCase {

    override suspend fun saveMovimentation(
        description: String,
        value: String,
        tag: Tag,
        type: Type
    ): Long {
        val doubleValue = value.toDouble() / 100
        val decimalValue = if (type == Type.GAIN) doubleValue else doubleValue.unaryMinus()
        val movimentation = Movimentation(
            value = decimalValue,
            description = description,
            tag = tag,
            spendAt = Calendar.getInstance().timeInMillis
        )
        return finnanceRepository.saveMovimentation(movimentation)
    }

    override fun getProfit(): Flow<List<MovimentationInfo>> = flow {
        finnanceRepository.getMovimentations().collect {
            emit(movimentationMapper.mapMovimentations(it.filter { movimentation -> movimentation.value > 0 }))
        }
    }

    override fun getLoss(): Flow<List<MovimentationInfo>> = flow {
        finnanceRepository.getMovimentations().collect {
            emit(movimentationMapper.mapMovimentations(it.filter { movimentation -> movimentation.value < 0 }))
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