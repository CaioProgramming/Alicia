package com.ilustris.alicia.features.user.domain.usecase

import com.ilustris.alicia.features.user.data.User
import kotlinx.coroutines.flow.Flow

interface UserUseCase {

    suspend fun updateUser(user: User)

    suspend fun saveUser(username: String, avatar: Int = 0) : Flow<Long>

    suspend fun getUserById() : Flow<User?>

}