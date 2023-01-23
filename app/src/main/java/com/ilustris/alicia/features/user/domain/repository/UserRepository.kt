package com.ilustris.alicia.features.user.domain.repository

import com.ilustris.alicia.features.user.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun updateUser(user: User)

    suspend fun saveUser(userName: String, avatar: Int): Long

    suspend fun getUserByUid(uid: Long) : Flow<User?>


}