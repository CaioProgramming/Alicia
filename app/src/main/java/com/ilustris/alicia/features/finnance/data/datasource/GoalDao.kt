package com.ilustris.alicia.features.finnance.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ilustris.alicia.features.finnance.data.model.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Insert
    fun saveGoal(goal: Goal): Long

    @Query("Select * from Goal ORDER BY createdAt DESC")
    fun getGaols(): Flow<List<Goal>>
}