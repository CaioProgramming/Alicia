package com.ilustris.alicia.features.user.domain.usecase

import com.ilustris.alicia.features.user.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserUseCase {

    suspend fun updateUser(user: User)

    suspend fun saveUser(username: String, avatar: Int = 0) : Long

    suspend fun getUserById() : User?

}