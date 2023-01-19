package com.ilustris.alicia.features.user.domain.repository

import com.ilustris.alicia.features.user.data.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun updateUser(user: User)

    suspend fun saveUser(userName: String, avatar: Int)

    suspend fun getUserByUid(uid: Int) : Flow<User>


}