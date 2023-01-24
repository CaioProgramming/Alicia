package com.ilustris.alicia.features.user.data.datasource

import androidx.room.*
import com.ilustris.alicia.features.user.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun saveUser(user: User) : Long

    @Delete
    suspend fun delete(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user WHERE uid = (:uid)")
    fun getUserById(uid: Long): Flow<User?>




}