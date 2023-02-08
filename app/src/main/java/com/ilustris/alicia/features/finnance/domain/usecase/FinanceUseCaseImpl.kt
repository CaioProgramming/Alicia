package com.ilustris.alicia.features.finnance.domain.usecase

import com.himanshoe.charty.circle.model.CircleData
import com.himanshoe.charty.line.model.LineData
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

class FinanceUseCaseImpl @Inject constructor(
    private val finnanceRepository: FinnanceRepository,
    private val movimentationMapper: MovimentationMapper
) : FinanceUseCase {

    override suspend fun saveMovimentation(
        description: String,
        value: String,
        tag: Tag,
        type: Type
    ): Long {
        val doubleValue = value.toDouble() / 100
        val decimalValue = if (type == Type.PROFIT) doubleValue else doubleValue.unaryMinus()
        val movimentation = Movimentation(
            value = decimalValue,
            description = description,
            tag = tag,
            spendAt = Calendar.getInstance().timeInMillis
        )
        return finnanceRepository.saveMovimentation(movimentation)
    }

    override suspend fun saveGoal(description: String, value: String, tag: Tag): Long {
        val doubleValue = value.toDouble() / 100
        val goal = Goal(
            description = description,
            value = doubleValue,
            tag = tag,
            createdAt = Calendar.getInstance().timeInMillis
        )
        return finnanceRepository.saveGoals(goal)
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

    override fun getAllMovimentations(): Flow<List<MovimentationInfo>> = flow {
        finnanceRepository.getMovimentations().collect {
            emit(movimentationMapper.mapMovimentations(it))
        }
    }

    override fun getMovimentationsByDay(): Flow<List<MovimentationInfo>> = flow {
        finnanceRepository.getMovimentations().collect {
            emit(movimentationMapper.mapMovimentationsByDay(it))
        }
    }

    override fun getMovimentationsChart(): Flow<List<LineData>> = flow {
        finnanceRepository.getMovimentations().collect {
            emit(movimentationMapper.mapMovimentationsToLineData(it))
        }
    }

    override fun getMovimentationsCircleChart(): Flow<List<CircleData>> = flow {
        finnanceRepository.getMovimentations().collect {
            emit(movimentationMapper.mapMovimentationsToCircleData(it))
        }
    }

    override fun getGoals(): Flow<List<Goal>> = finnanceRepository.getGoals()

}