package com.ilustris.alicia.features.finnance.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ilustris.alicia.features.finnance.data.model.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Insert
    fun saveGoal(goal: Goal): Long

    @Update
    fun updateGoal(goal: Goal)

    @Query("DELETE FROM Goal WHERE id = :id")
    fun deleteGoal(id: Long)

    @Query("Select * from Goal ORDER BY createdAt DESC")
    fun getGoals(): Flow<List<Goal>>

    @Query("Select * from Goal ORDER BY createdAt DESC")
    fun getGoalsSync(): List<Goal>

    @Query("Select * from Goal WHERE id = :id")
    fun getGoalById(id: Long): Flow<Goal>

    @Query("Select * from Goal WHERE id = :id")
    fun getGoalByIdSync(id: Long): Goal
}
