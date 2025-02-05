package com.ilustris.alicia.features.finnance.data.repository

import com.ilustris.alicia.features.finnance.data.datasource.GoalDao
import com.ilustris.alicia.features.finnance.data.datasource.MovimentationDao
import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import com.ilustris.alicia.features.finnance.domain.repository.FinnanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FinnanceRepositoryImpl
    @Inject
    constructor(
        private val movimentationDao: MovimentationDao,
        private val goalDao: GoalDao,
    ) : FinnanceRepository {
        override suspend fun saveMovimentation(movimentation: Movimentation): Long = movimentationDao.saveMovimentation(movimentation)

        override fun getMovimentations(): Flow<List<Movimentation>> = movimentationDao.getMovimentations()

        override fun getMovimentationsSync(): List<Movimentation> = movimentationDao.getMovimentationsSync()

        override fun getMovimentationById(id: Long): Flow<Movimentation> = movimentationDao.getMovimentationById(id)

        override fun getMovimentationByIdSync(id: Long) = movimentationDao.getMovimentationByIdSync(id)

        override fun getGoals(): Flow<List<Goal>> = goalDao.getGoals()

        override fun getGoalsSync(): List<Goal> = goalDao.getGoalsSync()

        override fun getGoalById(id: Long): Flow<Goal> = goalDao.getGoalById(id)

        override fun getGoalByIdSync(id: Long): Goal = goalDao.getGoalByIdSync(id)

        override fun saveGoal(goal: Goal): Long = goalDao.saveGoal(goal)

        override fun updateGoal(goal: Goal) = goalDao.updateGoal(goal)

        override fun deleteMovimentation(movimentation: Movimentation) = movimentationDao.deleteMovimentation(movimentation.id)
    }
