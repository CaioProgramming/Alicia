package com.ilustris.alicia.features.finnance.domain.repository

import com.ilustris.alicia.features.finnance.data.model.Goal
import com.ilustris.alicia.features.finnance.data.model.Movimentation
import kotlinx.coroutines.flow.Flow

interface FinnanceRepository {


   suspend fun saveMovimentation(movimentation: Movimentation): Long


   fun getMovimentations(): Flow<List<Movimentation>>

   fun getGoals(): Flow<List<Goal>>

   fun saveGoals(goal: Goal): Long

}