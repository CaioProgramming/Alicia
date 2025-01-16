package com.ilustris.alicia.features.finnance.domain.usecase

import com.himanshoe.charty.circle.model.CircleData
import com.himanshoe.charty.line.model.LineData
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.data.model.Tag
import com.ilustris.alicia.features.finnance.data.model.TagHelper
import com.ilustris.alicia.features.finnance.domain.data.GoalInfo
import com.ilustris.alicia.features.finnance.domain.data.MovimentationInfo
import com.ilustris.alicia.features.finnance.domain.mapper.MovimentationMapper
import com.ilustris.alicia.features.finnance.domain.repository.FinnanceRepository
import com.ilustris.alicia.features.messages.data.model.Type
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class FinanceUseCaseImpl
    @Inject
    constructor(
        private val finnanceRepository: FinnanceRepository,
        private val movimentationMapper: MovimentationMapper,
    ) : FinanceUseCase {
        override suspend fun saveMovimentation(
            description: String,
            value: String,
            tag: Tag,
            type: Type,
        ): Movimentation {
            val doubleValue = value.toDouble() / 100
            val movimentation =
                Movimentation(
                    value = doubleValue,
                    description = description,
                    tag = tag.name,
                    spendAt = Calendar.getInstance().timeInMillis,
                )
            finnanceRepository.saveMovimentation(movimentation)
            return movimentation
        }

        override suspend fun saveMovimentation(movimentation: Movimentation) = finnanceRepository.saveMovimentation(movimentation)

        override suspend fun saveGoal(
            description: String,
            value: String,
            tag: Tag,
        ): Long {
            val doubleValue = value.toDouble() / 100
            val goal =
                Goal(
                    name = description,
                    value = doubleValue,
                    tag = tag.name,
                    createdAt = Calendar.getInstance().timeInMillis,
                    badge = TagHelper.getRandomBadge(),
                )
            return finnanceRepository.saveGoal(goal)
        }

        override suspend fun saveGoal(goal: Goal) = finnanceRepository.saveGoal(goal)

        override suspend fun updateGoal(goal: Goal) = finnanceRepository.updateGoal(goal)

        override fun getProfit(): Flow<List<MovimentationInfo>> =
            flow {
                finnanceRepository.getMovimentations().collect {
                    emit(movimentationMapper.mapMovimentations(it.filter { movimentation -> movimentation.value > 0 }))
                }
            }

        override fun getLoss(): Flow<List<MovimentationInfo>> =
            flow {
                finnanceRepository.getMovimentations().collect {
                    emit(movimentationMapper.mapMovimentations(it.filter { movimentation -> movimentation.value < 0 }))
                }
            }

        override fun getAmount(): Flow<Double> =
            flow {
                finnanceRepository.getMovimentations().collect {
                    emit(it.sumOf { movimentation -> movimentation.value })
                }
            }

        override fun getAllMovimentations(): Flow<List<MovimentationInfo>> =
            flow {
                finnanceRepository.getMovimentations().collect {
                    emit(movimentationMapper.mapMovimentations(it))
                }
            }

        override fun getAllMovimentationsSync(): List<MovimentationInfo> {
            val movimentations = finnanceRepository.getMovimentationsSync()
            return movimentationMapper.mapMovimentations(movimentations)
        }

        override suspend fun getMovimentationById(id: Long) = finnanceRepository.getMovimentationById(id)

        override suspend fun getMovimentationByIdSync(id: Long) = finnanceRepository.getMovimentationByIdSync(id)

        override fun getMovimentationsByDay(): Flow<List<MovimentationInfo>> =
            flow {
                finnanceRepository.getMovimentations().collect {
                    emit(movimentationMapper.mapMovimentationsByDay(it))
                }
            }

        override fun getMovimentationsChart(): Flow<List<LineData>> =
            flow {
                finnanceRepository.getMovimentations().collect {
                    emit(movimentationMapper.mapMovimentationsToLineData(it))
                }
            }

        override fun getMovimentationsCircleChart(): Flow<List<CircleData>> =
            flow {
                finnanceRepository.getMovimentations().collect {
                    emit(movimentationMapper.mapMovimentationsToCircleData(it))
                }
            }

        override fun getGoals(): Flow<List<Goal>> = finnanceRepository.getGoals()

        override fun getGoalById(id: Long): Flow<Goal> = finnanceRepository.getGoalById(id)

        override fun getGoalByIdSync(id: Long) = finnanceRepository.getGoalByIdSync(id)

        override fun getGoalsInfo(): Flow<List<GoalInfo>> =
            flow {
                finnanceRepository.getGoals().collect {
                    emit(
                        movimentationMapper
                            .mapGoalsToInfo(it)
                            .sortedByDescending { info -> info.header },
                    )
                }
            }
    }
