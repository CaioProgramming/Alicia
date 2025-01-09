package com.ilustris.alicia.features.finnance.data.datasource

import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.utils.DatabaseBuilder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoalDaoImpl
    @Inject
    constructor(
        private val databaseBuilder: DatabaseBuilder,
    ) : GoalDao {
        private val database by lazy { databaseBuilder.buildDataBase() }

        override fun saveGoal(goal: Goal): Long = database.goalDao().saveGoal(goal)

        override fun updateGoal(goal: Goal) = database.goalDao().updateGoal(goal)

        override fun deleteGoal(id: Long) = database.goalDao().deleteGoal(id)

        override fun getGoals(): Flow<List<Goal>> = database.goalDao().getGoals()

        override fun getGoalsSync(): List<Goal> = database.goalDao().getGoalsSync()

        override fun getGoalById(id: Long): Flow<Goal> = database.goalDao().getGoalById(id)

        override fun getGoalByIdSync(id: Long): Goal = database.goalDao().getGoalByIdSync(id)
    }
