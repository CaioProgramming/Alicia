package com.ilustris.alicia.features.finnance.domain.repository

import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import kotlinx.coroutines.flow.Flow

interface FinnanceRepository {
    suspend fun saveMovimentation(movimentation: Movimentation): Long

    fun getMovimentations(): Flow<List<Movimentation>>

    fun getMovimentationsSync(): List<Movimentation>

    fun getMovimentationById(id: Long): Flow<Movimentation>

    fun getMovimentationByIdSync(id: Long): Movimentation

    fun getGoals(): Flow<List<Goal>>

    fun getGoalsSync(): List<Goal>

    fun getGoalById(id: Long): Flow<Goal>

    fun getGoalByIdSync(id: Long): Goal

    fun saveGoal(goal: Goal): Long

    fun updateGoal(goal: Goal)

    fun deleteMovimentation(movimentation: Movimentation)
}
